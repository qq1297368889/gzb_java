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

import gzb.tools.Config;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;


public class HTTPServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new ExceptionHandler());
        // 异常处理器
        if (!NettyServer.allowedDomains.contains("0.0.0.0")) {
            p.addLast(new HTTPDomainFilterHandler(NettyServer.allowedDomains));
        }
        if (Config.maxPostSize > 0) {
            p.addLast(new HttpObjectAggregator(Config.maxPostSize));
        }
        // 编解码器：将字节流转换为 HTTP 消息对象
        p.addLast(new HttpServerCodec());
        // 消息聚合器：将分段的 HTTP 消息聚合为一个完整的请求/响应
        p.addLast(new HttpObjectAggregator(Config.maxPostSize < 1 ? Integer.MAX_VALUE : Config.maxPostSize * 1024 * 1024));
        // 请求处理器
        p.addLast(new HTTPRequestHandlerV4());

        if (Config.compression) {
            p.addLast(new HttpContentCompressor(9, 15, 8, Config.compressionMinSize));
        }


    }
}