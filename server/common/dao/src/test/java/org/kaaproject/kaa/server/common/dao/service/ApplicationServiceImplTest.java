/*
 * Copyright 2014 CyberVision, Inc.
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

package org.kaaproject.kaa.server.common.dao.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.kaaproject.kaa.common.dto.ApplicationDto;
import org.kaaproject.kaa.common.dto.ConfigurationSchemaDto;
import org.kaaproject.kaa.common.dto.ProfileSchemaDto;
import org.kaaproject.kaa.common.dto.TenantDto;
import org.kaaproject.kaa.server.common.dao.exception.IncorrectParameterException;
import org.kaaproject.kaa.server.common.dao.AbstractTest;

@Ignore("This test should be extended and initialized with proper context in each NoSQL submodule")
public class ApplicationServiceImplTest extends AbstractTest {

    @Test
    public void testFindAppsByTenantId() {
        List<ApplicationDto> applications = applicationService.findAppsByTenantId("423421421");
        Assert.assertEquals(0, applications.size());
    }

    @Test
    public void testRemoveAppsByTenantId() {
        TenantDto tenant = generateTenant();
        ApplicationDto application = generateApplication(tenant.getId());
        applicationService.removeAppsByTenantId(tenant.getId());
        List<ApplicationDto> foundApplications = applicationService.findAppsByTenantId(tenant.getId());
        Assert.assertTrue(foundApplications.isEmpty());
        Assert.assertEquals(0, foundApplications.size());
        TenantDto foundTenant = userService.findTenantById(tenant.getId());
        Assert.assertNotNull(foundTenant);
        List<ProfileSchemaDto> foundProfileSchemas = profileService.findProfileSchemasByAppId(application.getId());
        Assert.assertEquals(0, foundProfileSchemas.size());
        List<ConfigurationSchemaDto> foundConfigSchemas = configurationService.findConfSchemasByAppId(application.getId());
        Assert.assertEquals(0, foundConfigSchemas.size());
    }

    @Test
    public void findAppsByTenantIdTest() {
        TenantDto tenant = generateTenant();
        String tenantId = tenant.getId();
        ApplicationDto application = generateApplication(tenantId);
        List<ApplicationDto> applications = applicationService.findAppsByTenantId(tenantId);
        Assert.assertEquals(1, applications.size());
        Assert.assertEquals(application.getId(), applications.get(0).getId());
    }

    @Test
    public void findAppByIdTest(){
        ApplicationDto application = generateApplication();
        ApplicationDto foundApp = applicationService.findAppById(application.getId());
        Assert.assertNotNull(foundApp);
        Assert.assertEquals(application.getId(), foundApp.getId());
    }

    @Test
    public void removeAppByIdTest(){
        ApplicationDto application = generateApplication();
        ApplicationDto foundApp = applicationService.findAppById(application.getId());
        Assert.assertNotNull(foundApp);
        Assert.assertEquals(application.getId(), foundApp.getId());
        applicationService.removeAppById(application.getId());

        foundApp = applicationService.findAppById(application.getId());
        Assert.assertNull(foundApp);
    }

    @Test
    public void findAppByApplicationTokenTest(){
        ApplicationDto application = generateApplication();
        ApplicationDto foundApp = applicationService.findAppByApplicationToken(application.getApplicationToken());
        Assert.assertNotNull(foundApp);
        Assert.assertEquals(application.getId(), foundApp.getId());
    }

    @Test(expected = IncorrectParameterException.class)
    public void saveAppTest() {
        ApplicationDto app = generateApplication();
        applicationService.saveApp(app);
    }
}
