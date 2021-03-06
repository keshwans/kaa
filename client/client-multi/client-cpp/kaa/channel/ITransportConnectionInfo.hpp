/*
 * Copyright 2014-2015 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License") = 0;
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

#ifndef I_TRANSPORT_CONNECTION_INFO_HPP_
#define I_TRANSPORT_CONNECTION_INFO_HPP_

#include <cstdint>
#include <vector>

#include <boost/shared_ptr.hpp>

#include "kaa/channel/ServerType.hpp"
#include "kaa/channel/TransportProtocolId.hpp"

namespace kaa {

/**
 * Interface to represent a server connection information.
 *
 * Used by @link IKaaDataChannel @endlink and @link IKaaChannelManager @endlink.
 */
class ITransportConnectionInfo
{
public:

    /**
     * Retrieves the channel's server type (i.e. OPERATIONS or BOOTSTRAP).
     *
     * @return The channel's server type.
     * @see ServerType
     *
     */
    virtual ServerType getServerType() = 0;

    /**
     * Retrieves the access point id (operations/bootstrap server id).
     *
     * @return The access point id.
     */
    virtual std::int32_t getAccessPointId() = 0;

    /**
     * Retrieves the @link TransportProtocolId @endlink.
     *
     * @return The transport protocol id.
     * @see TransportProtocolId
     *
     */
    virtual TransportProtocolId getTransportId() = 0;

    /**
     * Retrieves serialized connection data.
     *
     * Serialization may be specific for each transport protocol implementation.
     *
     * @return The serialized connection data.
     */
    virtual const std::vector<std::uint8_t>& getConnectionInfo() = 0;

    virtual ~ITransportConnectionInfo() {}
};

typedef std::shared_ptr<ITransportConnectionInfo> ITransportConnectionInfoPtr;

} /* namespace kaa */

#endif /* I_TRANSPORT_CONNECTION_INFO_HPP_ */
