= PRIS Plugin

PRIS Plugin that can be installed on OpenNMS Karaf Container

== Installation

.Clone from GitHub
[source, bash]
----
git clone https://github.com/cgorantla/pris-plugin.git
----

.Compile and install
[source, bash]
----
cd pris-plugin
mvn install
----

.Configure source and mapper
[source, bash]
----
echo 'source = xls
source.file = ~/home/chandra/pris/myInventory.xls
mapper = echo' > target/opennms/etc/org.opennms.plugins.feature.pris-myrouter.cfg
----

.Install the plugin
[source, bash]
----
feature:repo-addfeature:repo-add mvn:org.opennms.plugins.pris-parent/karaf-features/1.0.0-SNAPSHOT/xml
feature:install opennms-plugins-pris-xls
----

