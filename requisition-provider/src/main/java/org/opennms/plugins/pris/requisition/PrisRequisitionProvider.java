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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.opennms.integration.api.v1.config.requisition.Requisition;
import org.opennms.integration.api.v1.requisition.RequisitionProvider;
import org.opennms.integration.api.v1.requisition.RequisitionRequest;
import org.opennms.plugins.pris.api.Mapper;
import org.opennms.plugins.pris.api.MapperFactory;
import org.opennms.plugins.pris.api.Source;
import org.opennms.plugins.pris.api.SourceFactory;
import org.opennms.plugins.pris.mappers.EchoMapper;

import com.google.common.base.Strings;

public class PrisRequisitionProvider implements RequisitionProvider {

    private static final String TYPE_PRIS = "pris";
    private static final String SERVICE_NAME = "name";
    private static final String SOURCE = "source";
    private static final String MAPPER = "mapper";

    private Map<String, SourceFactory> sourceFactoryMap = new ConcurrentHashMap<>();
    private Map<String, MapperFactory> mapperFactoryMap = new ConcurrentHashMap<>();

    private final PrisServiceManager prisServiceManager;

    public PrisRequisitionProvider(PrisServiceManager prisServiceManager) {
        this.prisServiceManager = prisServiceManager;
    }

    @Override
    public String getType() {
        return TYPE_PRIS;
    }

    @Override
    public RequisitionRequest getRequest(Map<String, String> parameters) {
        String requisitionName = parameters.get(SERVICE_NAME);
        Map<String, String> requisitionProperties = new HashMap<>();
        Map<String, String> properties = prisServiceManager.getServiceProperties().get(requisitionName);
        if(properties != null) {
            requisitionProperties.putAll(properties);
        }
        requisitionProperties.put(SERVICE_NAME, requisitionName);
        SourceFactory sourceFactory = null;
        MapperFactory mapperFactory = null;
        String sourceName = requisitionProperties.get(SOURCE);
        String mapperName = requisitionProperties.get(MAPPER);
        if (!Strings.isNullOrEmpty(sourceName)) {
            sourceFactory = sourceFactoryMap.get(sourceName);
        }
        if (!Strings.isNullOrEmpty(mapperName)) {
            mapperFactory = mapperFactoryMap.get(mapperName);
        }
        if (sourceFactory != null && mapperFactory != null) {
            Source prisSource = sourceFactory.create(requisitionProperties);
            Mapper prisMapper = mapperFactory.create(requisitionProperties);
            return new PrisRequisitionRequest(prisSource, prisMapper);
        } else if (sourceFactory != null) {
            // If no mapper specified, use default EchoMapper.
            Source prisSource = sourceFactory.create(requisitionProperties);
            return new PrisRequisitionRequest(prisSource, new EchoMapper());
        }
        return new RequisitionRequest() {};
    }

    @Override
    public Requisition getRequisition(RequisitionRequest request) {
        if(!(request instanceof  PrisRequisitionRequest)) {
            throw new IllegalArgumentException("No matching requisition found in Pris");
        }
        PrisRequisitionRequest prisRequisitionRequest = (PrisRequisitionRequest) request;
        Source prisSource = prisRequisitionRequest.getSource();
        Mapper prisMapper = prisRequisitionRequest.getMapper();
        try {
            // TODO: Modify this when other mappers are defined.
            // Ideally mappers doesn't need any requisition here.
            return prisMapper.map(prisSource.dump(), null);
        } catch (Exception e) {
            //Add log
            throw new IllegalArgumentException("No matching requisition found in Pris ", e);
        }
    }

    @Override
    public byte[] marshalRequest(RequisitionRequest request) {
        return new byte[0];
    }

    @Override
    public RequisitionRequest unmarshalRequest(byte[] bytes) {
        return null;
    }


    @SuppressWarnings({ "rawtypes" })
    public void onBindSourceFactory(SourceFactory sourceFactory, Map properties) {
        sourceFactoryMap.put(sourceFactory.getIdentifier(), sourceFactory);
    }

    @SuppressWarnings({ "rawtypes" })
    public void onUnbindSourceFactory(SourceFactory sourceFactory, Map properties) {
        sourceFactoryMap.remove(sourceFactory.getIdentifier(), sourceFactory);
    }

    @SuppressWarnings({ "rawtypes" })
    public void onBindMapperFactory(MapperFactory mapperFactory, Map properties) {
        mapperFactoryMap.put(mapperFactory.getIdentifier(), mapperFactory);
    }

    @SuppressWarnings({ "rawtypes" })
    public void onUnbindMapperFactory(MapperFactory mapperFactory, Map properties) {
        mapperFactoryMap.remove(mapperFactory.getIdentifier(), mapperFactory);
    }

}
