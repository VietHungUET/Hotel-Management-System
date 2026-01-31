import React, { useState, useEffect, useRef } from 'react';
import './Chatbot.css';
import axiosClient from '../../api/axiosClient';
import ReactMarkdown from 'react-markdown';

const Chatbot = () => {
    const [isOpen, setIsOpen] = useState(false);
    const [messages, setMessages] = useState([
        { id: 1, text: "Xin chào! Tôi có thể giúp gì cho bạn về khách sạn hôm nay?", sender: 'bot' }
    ]);
    const [inputMessage, setInputMessage] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const messagesEndRef = useRef(null);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const toggleChat = () => {
        setIsOpen(!isOpen);
    };

    const handleSendMessage = async (e) => {
        e.preventDefault();
        if (!inputMessage.trim()) return;

        const userMsg = { id: Date.now(), text: inputMessage, sender: 'user' };
        setMessages(prev => [...prev, userMsg]);
        setInputMessage("");
        setIsLoading(true);

        try {
            const response = await axiosClient.post('/api/v1/chatbot/chat', {
                message: userMsg.text
            });

            const botMsg = {
                id: Date.now() + 1,
                text: response.message || "Xin lỗi, tôi không hiểu câu hỏi.",
                sender: 'bot'
            };
            setMessages(prev => [...prev, botMsg]);
        } catch (error) {
            console.error("Chat error:", error);
            const errorMsg = {
                id: Date.now() + 1,
                text: "Xin lỗi, hệ thống đang gặp sự cố. Vui lòng thử lại sau.",
                sender: 'bot'
            };
            setMessages(prev => [...prev, errorMsg]);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="chatbot-container">
            {isOpen && (
                <div className="chat-window">
                    <div className="chat-header">
                        <div className="header-info">
                            <img src="https://cdn-icons-png.flaticon.com/512/4712/4712035.png" alt="Bot" className="bot-avatar" />
                            <div>
                                <h3>Hotel Assistant</h3>
                                <div><span className="status-dot"></span><span className="status-text">Online</span></div>
                            </div>
                        </div>
                        <button className="close-btn" onClick={toggleChat}>✕</button>
                    </div>

                    <div className="chat-messages">
                        {messages.map((msg) => (
                            <div key={msg.id} className={`message ${msg.sender}`}>
                                <ReactMarkdown>{msg.text}</ReactMarkdown>
                            </div>
                        ))}
                        {isLoading && (
                            <div className="typing-indicator">
                                <div className="typing-dot"></div>
                                <div className="typing-dot"></div>
                                <div className="typing-dot"></div>
                            </div>
                        )}
                        <div ref={messagesEndRef} />
                    </div>

                    <form className="chat-input" onSubmit={handleSendMessage}>
                        <input
                            type="text"
                            placeholder="Nhập tin nhắn..."
                            value={inputMessage}
                            onChange={(e) => setInputMessage(e.target.value)}
                            disabled={isLoading}
                        />
                        <button type="submit" className="send-btn" disabled={isLoading || !inputMessage.trim()}>
                            <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
                                <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"></path>
                            </svg>
                        </button>
                    </form>
                </div>
            )}

            {!isOpen && (
                <div className="chatbot-icon" onClick={toggleChat}>
                    <img src="https://cdn-icons-png.flaticon.com/512/4712/4712035.png" alt="Chat" />
                </div>
            )}
        </div>
    );
};

export default Chatbot;
