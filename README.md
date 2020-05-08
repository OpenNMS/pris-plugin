
# PRIS Plugin

PRIS Plugin that can be installed and run on OpenNMS Karaf Container

[![OpenNMS](https://circleci.com/gh/OpenNMS/pris-plugin.svg?style=shield)](https://app.circleci.com/pipelines/github/OpenNMS/pris-plugin)

## Getting Started

git clone https://github.com/OpenNMS/pris-plugin.git

### Prerequisites

maven, java8, OpenNMS Horizon > 24.0.0


### Improvements

* Make PRIS compatible with OIA so that it can use same features that are already there.
* Can run on OpenNMS and all the configuration co-exists.
* No need to run another web-component


### Installing


```
git clone https://github.com/OpenNMS/pris-plugin.git
```

```
cd pris-plugin
```

```
mvn install
```

## Configure PRIS Resources.

* Load myRouter config into OpenNMS.
```
echo 'name = myRouter
source = xls
source.file = /home/chandra/Downloads/myInventory-metadata.xls
mapper = echo' > /opt/opennms/etc/org.opennms.plugins.feature.pris-myRouter.cfg
```

* Load myServer config into OpenNMS.
```
echo 'name = myServer
source = xls
source.file = /home/chandra/Downloads/myInventory-metadata.xls
mapper = echo' > /opt/opennms/etc/org.opennms.plugins.feature.pris-myServer.cfg
```

* Import the requisition
```
./target/opennms/bin/send-event.pl uei.opennms.org/internal/importer/reloadImport --parm 'url requisition://pris?name=myServer'
```

```
./target/opennms/bin/send-event.pl uei.opennms.org/internal/importer/reloadImport --parm 'url requisition://pris?name=myRouter'
```


### Deployment on OpenNMS


```
sudo cp kar/target/opennms-pris-plugin.kar ~/opt/opennms/deploy/
```


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Release

* Version number comes from the `pom.xml` <version> tag
* Merges to master trigger releases
* Merges to develop trigger pre-releases
* All other branches trigger just the build job
* Branch build artifacts are in CircleCI of the build

## Acknowledgments

* This code is derived from original PRIS code base : https://github.com/OpenNMS/opennms-provisioning-integration-server
* Modified to make use of OIA.
* Modified to make use of OSGI Services and Config.
* Added features and generate kar


## Further Development

* Add more Sources by implementing Source and SourceFactory.
* Expose SourceFactory as osgi service in blueprint.
* Can add more Mappers by implementing Mapper and MapperFactory
* Expose MapperFactory as osgi service in blueprint
* Add each source as different feature in karaf-features.

