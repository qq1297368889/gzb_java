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

import gzb.frame.netty.handler.ExceptionHandler;
import gzb.frame.netty.handler.HTTPDomainFilterHandler;
import gzb.frame.netty.handler.HTTPHandler;
import gzb.tools.Config;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

@ChannelHandler.Sharable
public class HTTPServerInitializerV2 extends ChannelInitializer<SocketChannel> {

    public static HTTPDomainFilterHandler domainFilterHandler = new HTTPDomainFilterHandler(HTTPServer.allowedDomains);
 
    public static HTTPHandler httpHandler = new HTTPHandler();
    public static ExceptionHandler exceptionHandler=new ExceptionHandler();
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast(exceptionHandler);
        // 异常处理器
        if (!HTTPServer.allowedDomains.contains("0.0.0.0")) {
            p.addLast(domainFilterHandler);
        }

        // 编解码器：将字节流转换为 HTTP 消息对象
        p.addLast(new HttpServerCodec());

        // 消息聚合器：将分段的 HTTP 消息聚合为一个完整的请求/响应
        p.addLast(new HttpObjectAggregator(Config.maxPostSize < 1 ? Integer.MAX_VALUE : Config.maxPostSize));

        // 请求处理器
        p.addLast(httpHandler);
        if (Config.compression) {
            p.addLast(new  HttpContentCompressor(6, 15, 8, Config.compressionMinSize));
        }

    }
}