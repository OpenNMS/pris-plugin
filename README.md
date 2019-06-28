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

### Installing on OpenNMS Container


```
feature:repo-add mvn:org.opennms.plugins.pris-parent/karaf-features/1.0.0-SNAPSHOT/xml
```

```
feature:install opennms-plugins-pris-xls
```


## Deployment

Using Kar


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management


## Authors

* **Chandra Gorantla**

