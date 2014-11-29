package com.larcloud.server.media.RtspServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.rtsp.RtspResponseEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ycd
 * Date: 14-7-1
 */
@Component
@ChannelHandler.Sharable
public class InMultiplexerHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = Logger.getLogger(InMultiplexerHandler.class.getName()+" [InMultiplexerHandler]");
    @Autowired
    private RtspRequestHandler rtspRequestHandler;

    private int lengthRtcpLeft=0;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        final int magic = (byteBuf.getUnsignedByte(byteBuf.readerIndex()));
        if(lengthRtcpLeft<=0){
            if (byteBuf.readableBytes() < 4) {
                return;
            }
           if (magic==0x24){
            byte byte1 = byteBuf.getByte(byteBuf.readerIndex()+2);
            byte byte2 = byteBuf.getByte(byteBuf.readerIndex()+3);
            int int1 = byte1 < 0 ? (256 + byte1) : byte1;
            int int2 = byte2 < 0 ? (256 + byte2) : byte2;
            int rtcpLength = (int1 << 8) + int2;
            lengthRtcpLeft = rtcpLength + 4 - byteBuf.readableBytes();
            ReferenceCountUtil.release(byteBuf);
            }
           else{
               super.channelRead(ctx,msg);
           }
        }else  {
            ReferenceCountUtil.release(byteBuf);
            lengthRtcpLeft -=byteBuf.readableBytes();
        }
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        lengthRtcpLeft=0;
        super.channelActive(ctx);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
       lengthRtcpLeft=0;
        super.channelInactive(ctx);
    }
}
