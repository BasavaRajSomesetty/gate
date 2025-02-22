/*
 * Copyright 2021 OpsMx, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.opsmx.spinnaker.gate.audit;

import com.opsmx.spinnaker.gate.enums.AuditEventType;
import com.opsmx.spinnaker.gate.feignclient.AuditService;
import com.opsmx.spinnaker.gate.model.OesAuditModel;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Component;

@Component
@EnableFeignClients(basePackageClasses = AuditService.class)
@ConditionalOnExpression("${services.auditservice.enabled:true}")
public class AuditRestApiHandler implements AuditHandler {

  @Autowired private AuditService auditService;

  @Override
  public void publishEvent(AuditEventType auditEventType, Object auditData) {
    OesAuditModel oesAuditModel = new OesAuditModel();
    oesAuditModel.setEventId(UUID.randomUUID().toString());
    oesAuditModel.setAuditData(auditData);
    oesAuditModel.setEventType(auditEventType);
    auditService.publishAuditData(oesAuditModel, "OES");
  }
}
