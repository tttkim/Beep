package com.example.beep.domain

import com.example.beep.data.dto.message.MessageRequest
import com.example.beep.data.repository.MessageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageUseCase @Inject constructor(private val messageRepository: MessageRepository) {
    fun getReceive(type: Int) = messageRepository.getReceiveMessage(type)
    fun getSend() = messageRepository.getSendMessage()
    fun changeTag(messageRequest: MessageRequest) = messageRepository.changeTag(messageRequest)
    fun deleteMessage(messageId: Long) = messageRepository.deleteMessage(messageId)
}