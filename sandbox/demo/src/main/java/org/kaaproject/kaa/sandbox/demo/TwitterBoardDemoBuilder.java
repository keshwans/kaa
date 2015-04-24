/*
 * Copyright 2014-2015 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaaproject.kaa.sandbox.demo;

import org.kaaproject.kaa.common.dto.*;
import org.kaaproject.kaa.server.common.admin.AdminClient;
import org.kaaproject.kaa.server.common.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TwitterBoardDemoBuilder extends AbstractDemoBuilder {

    private static final Logger logger = LoggerFactory.getLogger(TwitterBoardDemoBuilder.class);


    public TwitterBoardDemoBuilder() {
        super("demo/twitterboard");
    }

    @Override
    protected void buildDemoApplicationImpl(AdminClient client) throws Exception {
        logger.info("Loading 'Twitter board application' data...");

        loginTenantAdmin(client);

        ApplicationDto twitterBoardApplication = new ApplicationDto();
        twitterBoardApplication.setName("Twitter board");
        twitterBoardApplication = client.editApplication(twitterBoardApplication);

        sdkKey.setApplicationId(twitterBoardApplication.getId());
        sdkKey.setProfileSchemaVersion(1);
        sdkKey.setLogSchemaVersion(1);

        loginTenantDeveloper(client);

        logger.info("Creating configuration schema...");
        ConfigurationSchemaDto configurationSchema = new ConfigurationSchemaDto();
        configurationSchema.setApplicationId(twitterBoardApplication.getId());
        configurationSchema.setName("TwitterBoard schema");
        configurationSchema.setDescription("Default configuration schema for the twitter board application");
        configurationSchema = client.createConfigurationSchema(configurationSchema, getResourcePath("config_schema.avsc"));
        logger.info("Configuration schema version: {}", configurationSchema.getMajorVersion());
        sdkKey.setConfigurationSchemaVersion(configurationSchema.getMajorVersion());
        logger.info("Configuration schema was created.");

        logger.info("Creating notification schema...");
        NotificationSchemaDto notificationSchemaDto = new NotificationSchemaDto();
        notificationSchemaDto.setApplicationId(twitterBoardApplication.getId());
        notificationSchemaDto.setName("Twitter board notification schema");
        notificationSchemaDto.setDescription("Notification schema for Twitter board application");
        notificationSchemaDto = client.createNotificationSchema(notificationSchemaDto, getResourcePath("notification_schema.avsc"));
        sdkKey.setNotificationSchemaVersion(notificationSchemaDto.getMajorVersion());
        logger.info("Notification schema was created.");

        EndpointGroupDto baseEndpointGroup = null;
        List<EndpointGroupDto> endpointGroups = client.getEndpointGroups(twitterBoardApplication.getId());
        if (endpointGroups.size() == 1 && endpointGroups.get(0).getWeight() == 0) {
            baseEndpointGroup = endpointGroups.get(0);
        }

        if (baseEndpointGroup == null) {
            throw new RuntimeException("Can't get default endpoint group twitter board application!");
        }

        ConfigurationDto baseConfiguration = new ConfigurationDto();
        baseConfiguration.setApplicationId(twitterBoardApplication.getId());
        baseConfiguration.setEndpointGroupId(baseEndpointGroup.getId());
        baseConfiguration.setSchemaId(configurationSchema.getId());
        baseConfiguration.setMajorVersion(configurationSchema.getMajorVersion());
        baseConfiguration.setMinorVersion(configurationSchema.getMinorVersion());
        baseConfiguration.setDescription("Base twitter board configuration");
        String body = FileUtils.readResource(getResourcePath("config_data.json"));
        logger.info("Configuration body: [{}]", body);
        baseConfiguration.setBody(body);
        baseConfiguration.setStatus(UpdateStatus.INACTIVE);
        logger.info("Editing the configuration...");
        baseConfiguration = client.editConfiguration(baseConfiguration);
        logger.info("Configuration was successfully edited");
        logger.info("Activating the configuration");
        client.activateConfiguration(baseConfiguration.getId());
        logger.info("Configuration was activated");

        logger.info("Finished loading 'Twitter board application' data...");
    }
}