/*
 * Copyright 2016 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cd.go.contrib.elasticagents.openstack.requests;

import cd.go.contrib.elasticagents.openstack.AgentInstances;
import cd.go.contrib.elasticagents.openstack.PendingAgentsService;
import cd.go.contrib.elasticagents.openstack.PluginRequest;
import cd.go.contrib.elasticagents.openstack.RequestExecutor;
import cd.go.contrib.elasticagents.openstack.executors.CreateAgentRequestExecutor;
import cd.go.contrib.elasticagents.openstack.utils.OpenstackClientWrapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

/**
 * see https://plugin-api.gocd.io/current/elastic-agents/#create-agent
 */
public class CreateAgentRequest {
    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    private String autoRegisterKey;
    private Map<String, String> properties;
    private Map<String, Object> jobIdentifier;
    private String environment;

    public CreateAgentRequest() {

    }

    public CreateAgentRequest(String autoRegisterKey, Map<String, String> properties, Map<String, Object> jobIdentifier, String environment) {
        this.autoRegisterKey = autoRegisterKey;
        this.jobIdentifier = jobIdentifier;
        this.properties = properties;
        this.environment = environment;
    }

    public static CreateAgentRequest fromJSON(String json) {
        return GSON.fromJson(json, CreateAgentRequest.class);
    }

    public String autoRegisterKey() {
        return autoRegisterKey;
    }

    public Map<String, String> properties() {
        return properties;
    }

    public String environment() {
        return environment;
    }

    public RequestExecutor executor(PendingAgentsService pendingAgents, AgentInstances agentInstances, PluginRequest pluginRequest) throws Exception {
        return new CreateAgentRequestExecutor(this, agentInstances, pluginRequest, new OpenstackClientWrapper(pluginRequest.getPluginSettings()), pendingAgents);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CreateAgentRequest{");
        sb.append("autoRegisterKey='").append(autoRegisterKey).append('\'');
        sb.append(", jobIdentifier=").append(jobIdentifier);
        sb.append(", properties=").append(properties);
        sb.append(", environment='").append(environment).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Map<String, Object> job() {
        return jobIdentifier;
    }

    public boolean jobMatches(Map<String, Object> otherJob) {
        if(jobIdentifier.size() != otherJob.size())
            return false;
        return jobIdentifier.entrySet().containsAll(otherJob.entrySet());

    }
}
