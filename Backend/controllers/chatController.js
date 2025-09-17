const Chat = require("../models/chat");
const { successResponse, errorResponse } = require("../utils/response.js");
const { GoogleGenerativeAI } = require("@google/generative-ai");

const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY);
const model = genAI.getGenerativeModel({ model: "gemini-pro" });

const seriousKeywords = new Set([
  "suicide",
  "kill myself",
  "end my life",
  "depression",
  "anxiety attack",
  "panic attack",
  "chronic stress",
  "self harm",
]);

const botReply = async (message) => {
  const lowerCaseMessage = message.toLowerCase();

  // Check for serious keywords first
  for (const keyword of seriousKeywords) {
    if (lowerCaseMessage.includes(keyword)) {
      return "It sounds like you are going through a difficult time. I strongly recommend you book an appointment with one of our doctors for a consultation. They are here to help you.";
    }
  }

  // --- LLM Integration ---
  // If no serious keywords are found, call the Gemini LLM for a dynamic response.
  try {
    const prompt = `You are a caring and supportive chatbot for a student mental wellness app called eUdayan. A user said: "${message}". Respond in a gentle, empathetic, and brief manner. Do not give medical advice.`;
    const result = await model.generateContent(prompt);
    const response = await result.response;
    const text = response.text();
    return text;
  } catch (error) {
    console.error("Error calling Gemini API:", error);
    // Fallback response in case the LLM fails
    return "I'm here to listen. Could you tell me a little more about what's on your mind?";
  }
};

// Send message
exports.sendMessage = async (req, res) => {
  try {
    const { receiverId, message } = req.body; // receiverId is null for bot chat

    const newMessage = await Chat.create({
      senderId: req.user.id,
      receiverId: receiverId || null,
      message,
      type: "student",
    });

    let botMessage = null;
    if (!receiverId) {
      // If no receiver, it's a chat with the bot.
      const replyMessage = await botReply(message);
      botMessage = await Chat.create({
        senderId: null,
        receiverId: req.user.id,
        message: replyMessage,
        type: "bot",
      });
    }

    return successResponse(res, "Message sent successfully", { newMessage, botMessage }, 201);
  } catch (err) {
    console.error(err);
    return errorResponse(res, "Error sending message");
  }
};

// Get messages for a specific conversation
exports.getMessages = async (req, res) => {
  try {
    const { receiverId } = req.params;
    const senderId = req.user.id;

    // Build a query to find messages between the two parties.
    // For bot chat, the frontend can pass 'bot' as the receiverId.
    const query =
      receiverId === 'bot'
        ? {
            $or: [{ senderId, receiverId: null }, { senderId: null, receiverId: senderId }],
          }
        : {
            $or: [{ senderId, receiverId }, { senderId: receiverId, receiverId: senderId }],
          };

    const messages = await Chat.find(query).sort({ createdAt: 1 });
    return successResponse(res, "Messages fetched successfully", messages);
  } catch (err) {
    console.error(err);
    return errorResponse(res, "Error fetching messages");
  }
};
