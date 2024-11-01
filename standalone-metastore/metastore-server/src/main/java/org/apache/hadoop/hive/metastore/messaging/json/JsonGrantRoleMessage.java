/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.metastore.messaging.json;

import org.apache.hadoop.hive.metastore.api.PrincipalType;
import org.apache.hadoop.hive.metastore.api.Role;
import org.apache.hadoop.hive.metastore.messaging.GrantRoleMessage;
import org.apache.hadoop.hive.metastore.messaging.MessageBuilder;
import org.apache.thrift.TException;
import org.codehaus.jackson.annotate.JsonProperty;

public class JsonGrantRoleMessage extends GrantRoleMessage {
  @JsonProperty
  private String server, servicePrincipal, roleObj, principalName, grantor;

  @JsonProperty
  private PrincipalType principalType, grantorType;

  @JsonProperty
  private boolean grantOption;

  @JsonProperty
  private Long timestamp;

  public JsonGrantRoleMessage() {}

  public JsonGrantRoleMessage(String server, String servicePrincipal, Role role, String principalName, PrincipalType principalType, String grantor,
      PrincipalType grantorType, boolean grantOption, Long timestamp) {
    this.server = server;
    this.servicePrincipal = servicePrincipal;
    this.principalName = principalName;
    this.principalType = principalType;
    this.grantor = grantor;
    this.grantorType = grantorType;
    this.grantOption = grantOption;
    this.timestamp = timestamp;
    try {
      this.roleObj = MessageBuilder.createRoleObjJson(role);
    } catch (TException e) {
      throw new IllegalArgumentException("Could not serialize: ", e);
    }
    checkValid();
  }

  @Override
  public String getServer() {
    return server;
  }

  @Override
  public String getServicePrincipal() {
    return servicePrincipal;
  }

  @Override
  public String getDB() {
    return null;
  }

  @Override
  public Long getTimestamp() {
    return timestamp;
  }

  @Override
  public Role getRole() throws Exception {
    return (Role) MessageBuilder.getTObj(roleObj, Role.class);
  }

  public String getPrincipalName() {
    return principalName;
  }

  public PrincipalType getPrincipalType() {
    return principalType;
  }

  public String getGrantor() {
    return grantor;
  }

  public PrincipalType getGrantorType() {
    return grantorType;
  }

  public boolean isGrantOption() {
    return grantOption;
  }

  @Override
  public String toString() {
    try {
      return JSONMessageDeserializer.mapper.writeValueAsString(this);
    } catch (Exception exception) {
      throw new IllegalArgumentException("Could not serialize: ", exception);
    }
  }
}
