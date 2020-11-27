/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.net.websocket.observability;

import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.observability.ObserveUtils;
import io.ballerina.runtime.observability.ObserverContext;
import org.ballerinalang.net.websocket.server.WebSocketConnectionInfo;

/**
 * Providing tracing functionality to WebSockets.
 *
 * @since 1.1.0
 */

public class WebSocketTracingUtil {

    /**
     * Obtains the current observer context of new resource that was invoked and sets the necessary tags to it.
     *
     * @param strand         Strand of the new resource invoked
     * @param connectionInfo information regarding connection.
     */
    static void traceResourceInvocation(Environment environment, WebSocketConnectionInfo connectionInfo) {
        if (!ObserveUtils.isTracingEnabled()) {
            return;
        }
        ObserverContext observerContext = ObserveUtils.getObserverContextOfCurrentFrame(environment);
        if (observerContext != null) {
            setTags(observerContext, connectionInfo);
            return;
        }
        observerContext = new ObserverContext();
        ObserveUtils.setObserverContextToCurrentFrame(environment, observerContext);
        setTags(observerContext, connectionInfo);
    }

    private static void setTags(ObserverContext observerContext, WebSocketConnectionInfo connectionInfo) {
        observerContext.addTag(WebSocketObservabilityConstants.TAG_CONTEXT,
               WebSocketObservabilityUtil.getClientOrServerContext(connectionInfo));
        observerContext.addTag(WebSocketObservabilityConstants.TAG_SERVICE,
                               WebSocketObservabilityUtil.getServicePathOrClientUrl(connectionInfo));
    }


    private WebSocketTracingUtil() {

    }
}
