/*
 * Copyright 2022 Netflix, Inc.
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

package com.opsmx.spinnaker.gate.interceptors;

import com.opsmx.spinnaker.gate.exception.XSpinnakerUserHeaderMissingException;
import com.opsmx.spinnaker.gate.rbac.ApplicationFeatureRbac;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@ConditionalOnExpression("${rbac.enabled:false}")
public class CustomGatesTriggerRbacInterceptor implements HandlerInterceptor {

  @Autowired private ApplicationFeatureRbac applicationFeatureRbac;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    Optional.ofNullable(request.getHeader("x-spinnaker-user"))
        .orElseThrow(
            () -> new XSpinnakerUserHeaderMissingException("x-spinnaker-user header missing"));

    try {
      String x_spinnaker_user = request.getHeader("x-spinnaker-user");
      log.info(
          "Request intercepted for authorizing if the user is having enough access to perform the action");
      //      applicationFeatureRbac.authorizeUserForApprovalGateTrigger(
      //        x_spinnaker_user, request.getRequestURI());
    } catch (NumberFormatException nfe) {
      log.debug("Ignoring the rbac check as it threw number format exception");
    }

    return true;
  }
}