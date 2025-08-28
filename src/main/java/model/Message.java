/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author ADMIN
 */
public class Message {

    private int id;
    private String senderEmail;  // không null
    private String recipientEmail;
    private String subject;      // bắt buộc có
    private String content;
    private Timestamp sentTime;
    private boolean isRead;
    private String guest_label;
    private String senderName;
    private String recipientName;
    private String senderType;
    Integer SenderId;
    Integer RecipientId;
    String messageType;

    public Integer getSenderId() {
        return SenderId;
    }

    public void setSenderId(Integer SenderId) {
        this.SenderId = SenderId;
    }

    public Integer getRecipientId() {
        return RecipientId;
    }

    public void setRecipientId(Integer RecipientId) {
        this.RecipientId = RecipientId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String recipientEmail() {
        return senderEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getSentTime() {
        return sentTime;
    }

    public void setSentTime(Timestamp sentTime) {
        this.sentTime = sentTime;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Message(int id, String senderEmail, String recipientEmail, String subject, String content, Timestamp sentTime, boolean isRead, String guest_label, String senderName, String recipientName, String senderType, int SenderId, int RecipientId, String messageType) {
        this.id = id;
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.content = content;
        this.sentTime = sentTime;
        this.isRead = isRead;
        this.guest_label = guest_label;
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.senderType = senderType;
        this.SenderId = SenderId;
        this.RecipientId = RecipientId;
        this.messageType = messageType;
    }

    public Message() {
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getGuest_label() {
        return guest_label;
    }

    public void setGuest_label(String guest_label) {
        this.guest_label = guest_label;
    }
}
