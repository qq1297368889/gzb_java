/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.frame.netty;

import gzb.frame.netty.handler.*;
import gzb.tools.Config;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.flush.FlushConsolidationHandler;
/// 性能优化麻烦 暂时搁置
@ChannelHandler.Sharable
public class HTTPServerInitializerV2 extends ChannelInitializer<SocketChannel> {

    public static HTTPDomainFilterHandler domainFilterHandler = new HTTPDomainFilterHandler(HTTPServer.allowedDomains);

    public static HTTPHandlerV2 httpHandler = new HTTPHandlerV2();
    public static ExceptionHandler exceptionHandler=new ExceptionHandler();

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new FlushConsolidationHandler(Config.PIPELINE, true));

        p.addLast(new GzbHttpDecoder());
        p.addLast(httpHandler);

        p.addLast(exceptionHandler);
    }
}