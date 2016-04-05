package com.newman.netty4.test;

import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.STRING;

import com.google.protobuf.InvalidProtocolBufferException;

public class TestSubscribeReqProto {
//
//	private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
//		return req.toByteArray();
//	}
//
//	private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
//		return SubscribeReqProto.SubscribeReq.parseFrom(body);
//	}
//
//	private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
//		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
//		builder.setSubReqID(1);
//		builder.setUserName("newman");
//		builder.setProductName("Netty Book");
//		List<String> address = new ArrayList<String>();
//		address.add("address1");
//		address.add("address2");
//		address.add("address3");
//		builder.addAllAddress(address);
//		return builder.build();
//	}
//
//	public static void main(String[] args) throws InvalidProtocolBufferException {
//		SubscribeReqProto.SubscribeReq req = createSubscribeReq();
//		System.out.println("Before encode: " + req.toString());
//		SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
//		System.out.println("After decode: " + req.toString());
//
//		System.out.println("Assert equal: " + req.equals(req2));
//	}
}
