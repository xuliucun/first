package com.learn.ionio.socketNIO.chat;

import java.io.IOException;
import java.nio.channels.SelectionKey;

//对selectionKey事件的处理

interface ServerHandlerBs {
  void handleAccept(SelectionKey selectionKey) throws IOException;

  String handleRead(SelectionKey selectionKey) throws IOException;
}

