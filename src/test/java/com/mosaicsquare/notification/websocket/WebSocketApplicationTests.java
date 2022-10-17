package com.mosaicsquare.websocket;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebSocketApplicationTests {
//ToDo 서버를 띄워서 내부 상태를 검증하던 로직을 테스트 로직으로 변경한다.
//    @Autowired
//    NettyServer nettyServer;
//
//    private static List<Socket> listClient = new LinkedList<Socket>();
//    private static String currentRoomName = "common";
//
//    private static Integer connectedCnt = 0 , recvedCnt = 0;
//
//    public static void JoinRoom(Socket client, String roomName) {
//        if ( false == roomName.isEmpty() ) {
//            client.on(roomName, new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    //System.out.println("recv : " + args[0].toString());
//                    recvedCnt++;
//                }
//            });
//            //client.emit("joinSUB", new ChatMsg(roomName, "user_" + client.toString(), "").toJSON());
//            client.emit("joinSUB", roomName);
//        }
//    }
//
//    public static void LeaveRoom(Socket client, String roomName) {
//        if ( false == roomName.isEmpty() ) {
//            client.off(roomName);
//            //client.emit("leaveSUB", new ChatMsg(roomName, "user_" + client.toString(), "").toJSON());
//            client.emit("leaveSUB", roomName);
//        }
//    }
//
//    public static void SendMessage(Socket client , String roomName , String message ) {
//        if ( false == roomName.isEmpty() ) {
//            client.emit("notifySUB", new SubscriptionMsg(roomName, "notyfi" , message).toJSON());
//            //client.emit(roomName, new ChatMsg(roomName, "user_" + client.toString(), message).toJSON());
//        }
//    }
//
//    public static Socket InitSocket(final String serverIP, final Integer serverPort) {
//
//        IO.Options opts = new IO.Options();
//        opts.forceNew = true;
//        opts.reconnection = true;
//        opts.transports = new String[]{"websocket"};
//
//        String url = "http://" + serverIP + ":" + serverPort.toString();
//        final Socket client = IO.socket(URI.create(URI.create(url) + "/"), opts);
//
//        //ready
//        client.on(Socket.EVENT_CONNECTING, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_CONNECTING : " + args.toString() + " : " + client.toString());
//                connectedCnt++;
//            }
//        });
//
//        //connect
//        client.on(Socket.EVENT_CONNECTING, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_CONNECTING : " + args.toString() + " : " + client.toString());
//            }
//        }).on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_CONNECT : " + args.toString() + " : " + client.toString());
//                JoinRoom(client, currentRoomName);
//
//            }
//        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_CONNECT_ERROR : " + args[0].toString() + " : " + client.toString());
//                //client.close();
//            }
//        }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_CONNECT_TIMEOUT : " + args[0].toString() + " : " + client.toString());
//                //client.close();
//            }
//        });
//
//        //reconnect
//        client.on(Socket.EVENT_RECONNECT_ATTEMPT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_RECONNECT_ATTEMPT : " + args[0].toString() + " : " + client.toString());
//            }
//        }).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_RECONNECT : " + args[0].toString() + " : " + client.toString());
//                JoinRoom(client, currentRoomName);
//            }
//        }).on(Socket.EVENT_RECONNECT_ERROR, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_RECONNECT_ERROR : " + args[0].toString() + " : " + client.toString());
//                //client.close();
//            }
//        }).on(Socket.EVENT_RECONNECT_FAILED, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_RECONNECT_FAILED : " + args.toString() + " : " + client.toString());
//                //client.close();
//            }
//        });
//
//        //recv data
//        client.on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_MESSAGE : " + args[0].toString() + " : " + client.toString());
//            }
//        });
//
//        //status event
//        client.on(Socket.EVENT_PING, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_PING : " + args.toString() + " : " + client.toString());
//            }
//        }).on(Socket.EVENT_PONG, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_PONG : " + args.toString() + " : " + client.toString());
//            }
//        });
//
//        //disconnect
//        client.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_DISCONNECT : " + args[0].toString() + " : " + client.toString());
//                //client.close();
//            }
//        });
//
//        //exception
//        client.on(Socket.EVENT_ERROR, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                //System.out.println("user EVENT_ERROR : " + args[0].toString() + " : " + client.toString());
//                //client.close();
//            }
//        });
//
//        //System.out.println("user try EVENT_CONNECT : " + client.toString());
//        client.connect();
//        return client;
//    }
//
//    @Test
//    void contextLoads() {
//
//        Integer testCnt = 50;
//
//        try {
//            nettyServer.ServerStart();
//
//            for (Integer idx = 0; idx < 20; idx++) {
//                for (Integer i = 0; i < testCnt; i++) {
//                    Socket client = InitSocket(nettyServer.getServerHost(), nettyServer.getServerPort());
//                    listClient.add(client);
//                }
//
//                Thread.sleep(10000);
//                System.out.println("connected : " + connectedCnt);
//                if (testCnt == connectedCnt) {
//                    for (Socket client : listClient) {
//                        if (true == client.connected()) {
//                            SendMessage(client, currentRoomName, "scheshulde sending test : [" + client.toString() + "]");
//                        }
//                    }
//                    Thread.sleep(10000);
//                } else {
//                    throw new Exception("connection test failed");
//                }
//
//                System.out.println("connected : " + connectedCnt + " , recv count : " + recvedCnt);
//                if (recvedCnt != testCnt * testCnt) {
//                    throw new Exception("broadcast test failed");
//                }
//
//                for (Socket client : listClient) {
//                    if (true == client.connected()) {
//                        client.close();
//                    }
//                }
//                listClient.clear();
//                recvedCnt = 0;
//                connectedCnt = 0;
//            }
//        } catch (Exception e) {
//        } finally {
//            nettyServer.ServerStop();
//        }
//    }
}
