/*
 * Main.cc
 *
 *  Created on: 2016年4月1日
 *      Author: newman
 */

#include <iostream>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <stdio.h>
#include <sys/socket.h>
#include <string.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include "PROTOCOL.pb.h"

//#define __DEBUG	1

using namespace std;
using namespace Netty4;

size_t send_protocol_message (int fd, ProtocolMessage pm) {
#define BUFFER_SIZE 1024
#define INT_SIZEOF	4

	char buffer[BUFFER_SIZE] = { 0 };
	memset(buffer, 0x00, sizeof(buffer));

	pm.SerializePartialToArray(buffer, sizeof(buffer));
	size_t buffer_len = strlen(buffer);
	size_t bl = htonl(buffer_len);
	// 150994944
	cout << "send_protocol_message - int length: " << *(size_t*)&bl << ", buffer_len: " << buffer_len << endl;
	int writen = write(fd, (const void*) &bl, INT_SIZEOF);
	writen = write(fd, buffer, strlen(buffer));

	cout << "writen: " << writen << endl;

	return (writen);
}

int recv_protocol_message (int fd, ProtocolMessage& pm) {
#define BUFFER_SIZE 1024
#define INT_SIZEOF	4

	char buffer[BUFFER_SIZE] = { 0 };
	memset(buffer, 0x00, sizeof(buffer));

	int recvbytes;
	size_t head_len;
	char recv_pm_len_arr[64] = { 0 };
	if (-1 == (recvbytes = read(fd, recv_pm_len_arr, INT_SIZEOF))) {
		printf("read len fail !\r\n");
		return -1;
	}
	recv_pm_len_arr[recvbytes] = 0x00;
	head_len = htonl(*(int*) recv_pm_len_arr);
	cout << "recv_pm_len: " << head_len << endl;

	memset(buffer, 0x00, sizeof(buffer));
	if (-1 == (recvbytes = read(fd, buffer, head_len))) {
		printf("read data fail !\r\n");
		return -1;
	}
	buffer[recvbytes] = '\0';

	string recv_str = buffer;
	pm.ParseFromString(recv_str);

	return (0);
}

void debug_protocol_message (ProtocolMessage pm) {
	switch (pm.type()) {
		case CMT_S_LOGIN:
			cout << "===== DEBUG FOR `CMT_S_LOGIN` =====" << endl;
			cout << "rlt_code: " << pm.resp_login().rlt().code() << endl;
			cout << "rlt_msg: " << pm.resp_login().rlt().msg() << endl;
			break;
		case CMT_S_PPTLIST:
			cout << "===== DEBUG FOR `CMT_S_PPTLIST` =====" << endl;
			cout << "rlt_code: " << pm.resp_ppt_list().rlt().code() << endl;
			cout << "rlt_msg: " << pm.resp_ppt_list().rlt().msg() << endl;
			cout << "tag: " << pm.resp_ppt_list().tag() << endl;
			cout << "desc: " << pm.resp_ppt_list().desc() << endl;
			cout << "node: " << endl;
			for (int i=0; i<pm.resp_ppt_list().node_size(); ++i) {
				cout << "\tnode" << i << " - id : " << pm.resp_ppt_list().node(i).id() << endl;
				cout << "\tnode" << i << " - image : " << pm.resp_ppt_list().node(i).image() << endl;
				cout << "\tnode" << i << " - order : " << pm.resp_ppt_list().node(i).order() << endl;
			}
			break;
		case CMT_C_LOGIN:
			cout << "===== DEBUG FOR `CMT_C_LOGIN` =====" << endl;
			cout << "id: " << pm.req_login().id() << endl;
			cout << "token: " << pm.req_login().token() << endl;
			cout << "app version: " << pm.req_login().app_version() << endl;
			cout << "platform: " << pm.req_login().platform() << endl;
			break;

		case CMT_C_PPTLIST:
			cout << "===== DEBUG FOR `CMT_C_PPTLIST` =====" << endl;
			cout << "userid: " << pm.req_ppt_list().userid() << endl;
			cout << "vspid: " << pm.req_ppt_list().vspid() << endl;
			break;

		default:
			cout << "Don't know message type!" << endl;
			break;
	}
}

int main(void) {

	GOOGLE_PROTOBUF_VERIFY_VERSION;

	// Fill Login Protocol
	ProtocolMessage pm_login;
	ReqLogin login;
	login.set_id(90);
	login.set_token("C++ SKDJFKSJDFKJSKDJF");
	login.set_app_version("v1.3");
	login.set_platform("android v4.5 versi的");
	pm_login.set_type(CMT_C_LOGIN);
	pm_login.set_allocated_req_login(&login);

	// Fill PPTList Protocol
	ProtocolMessage pm_pptlist;
	ReqPPTList pptlist;
	pptlist.set_userid(100);
	pptlist.set_vspid(200);
	pm_pptlist.set_type(CMT_C_PPTLIST);
	pm_pptlist.set_allocated_req_ppt_list(&pptlist);

#ifdef __DEBUG
	debug_protocol_message(pm_login);
	debug_protocol_message(pm_pptlist);
#endif

	int cfd;
	char buffer[1024] = { 0 };
	struct sockaddr_in s_add;
	unsigned short portnum = 8765;

	memset(buffer, 0x00, sizeof(buffer));
#ifdef __DEBUG
	printf("Hello,welcome to client !\r\n");
#endif
	cfd = socket(AF_INET, SOCK_STREAM, 0);
	if (-1 == cfd) {
		printf("socket fail ! \r\n");
		return -1;
	}
#ifdef __DEBUG
	printf("socket ok !\r\n");
#endif
	bzero(&s_add, sizeof(struct sockaddr_in));
	s_add.sin_family = AF_INET;
	s_add.sin_addr.s_addr = inet_addr("127.0.0.1");
	s_add.sin_port = htons(portnum);

	if (connect(cfd, (struct sockaddr *) (&s_add), sizeof(struct sockaddr))
			== -1) {
		printf("connect fail !\r\n");
		return -1;
	}
#ifdef __DEBUG
	printf("connect ok !\r\n");
#endif
	// For Login Protocol
	send_protocol_message(cfd, pm_login);
	ProtocolMessage recv_pm_login;
	recv_protocol_message(cfd, recv_pm_login);
	debug_protocol_message(recv_pm_login);

	// For PPTList Protocol
	send_protocol_message(cfd, pm_pptlist);
	ProtocolMessage recv_pm_pptlist;
	recv_protocol_message(cfd, recv_pm_pptlist);
	debug_protocol_message(recv_pm_pptlist);

	if (cfd > 0) {
		close(cfd);
	}

	google::protobuf::ShutdownProtobufLibrary();

	return 0;
}
