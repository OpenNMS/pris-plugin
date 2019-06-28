# PRIS Plugin

PRIS Plugin that can be installed and run on OpenNMS Karaf Container

## Getting Started

git clone https://github.com/cgorantla/pris-plugin.git

### Prerequisites

maven, java8, OpenNMS Horizon > 24.0.0


### Installing


```
git clone https://github.com/cgorantla/pris-plugin.git
```

```
cd pris-plugin
```

```
mvn install
```

## Configure PRIS Resources.


```
echo 'source = xls
source.file = /home/chandra/pris/myInventory.xls
mapper = echo' > /opt/opennms/etc/org.opennms.plugins.feature.pris-myrouter.cfg
```

### Deployment on OpenNMS


```
cp kar/target/opennms-pris-plugin.kar ~/opt/opennms/deploy/
```

```
feature:install opennms-plugins-pris-xls
```

* **To sustain restarts, add `opennms-plugins-pris-xls` feature to `org.apache.karaf.features.cfg`**


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management


## Authors

* **Chandra Gorantla**

