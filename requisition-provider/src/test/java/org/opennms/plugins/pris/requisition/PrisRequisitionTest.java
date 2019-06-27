/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.plugins.pris.requisition;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.opennms.integration.api.v1.config.requisition.Requisition;
import org.opennms.integration.api.v1.config.requisition.RequisitionNode;
import org.opennms.integration.api.v1.requisition.RequisitionRequest;
import org.opennms.plugins.pris.api.MapperFactory;
import org.opennms.plugins.pris.api.SourceFactory;
import org.opennms.plugins.pris.mappers.EchoMapperFactory;
import org.opennms.plugins.pris.xls.XlsSourceFactory;
import org.osgi.service.cm.ConfigurationException;

import com.google.common.io.Resources;

public class PrisRequisitionTest {

    @Test
    public void testPrisRequisitionWithXls() throws ConfigurationException {

        PrisServiceManager prisServiceManager = new PrisServiceManager();
        Dictionary<String, String> params = new Hashtable<>();
        URL resourceURL = Resources.getResource("myInventory.xls");
        System.out.println(resourceURL.getPath());
        params.put("name", "myRouter");
        params.put("source", "xls");
        params.put("source.file", resourceURL.getPath());
        prisServiceManager.updated("myRouter", params);
        Map<String, String> properties = new HashMap<>();
        properties.put("name", "myRouter");
        PrisRequisitionProvider requisitionProvider = new PrisRequisitionProvider(prisServiceManager);
        SourceFactory sourceFactory = new XlsSourceFactory();
        MapperFactory mapperFactory = new EchoMapperFactory();
        requisitionProvider.onBindSourceFactory(sourceFactory, new HashMap());
        requisitionProvider.onBindMapperFactory(mapperFactory, new HashMap());
        RequisitionRequest requisitionRequest = requisitionProvider.getRequest(properties);
        Requisition requisition = requisitionProvider.getRequisition(requisitionRequest);
        assertNotNull(requisition);
        assertThat(requisition.getNodes(), hasSize(4));
        List<RequisitionNode> nodeList = requisition.getNodes();
        List<String> ipList = new ArrayList<>();
        Set<String> categories = new HashSet<>();
        nodeList.forEach(node -> {
            node.getCategories().forEach(categories::add);
            node.getInterfaces().forEach(requisitionInterface -> {
                ipList.add(requisitionInterface.getIpAddress().getHostAddress());
            });
        });
        assertThat(ipList, containsInAnyOrder("172.16.23.2",
                                                     "172.16.23.3",
                                                     "192.168.30.1",
                                                     "172.16.23.1",
                                                     "10.0.23.2",
                                                     "10.0.23.1"));
        assertThat(categories, containsInAnyOrder("Backbone", "Office"));


    }
}
