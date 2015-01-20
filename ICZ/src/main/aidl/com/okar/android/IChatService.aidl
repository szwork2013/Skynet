package com.okar.android;

import com.okar.po.Packet;

interface IChatService{
    void sendMessage(String message);
    void sendPacket(in Packet packet);
}