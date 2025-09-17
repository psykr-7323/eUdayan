const express = require("express");
const { sendMessage, getMessages } = require("../controllers/chatController.js");
const authMiddleware = require("../middleware/authMiddleware.js");

const router = express.Router();

// ✅ Send a new chat message (student/doctor/bot)
router.post("/", authMiddleware.protect, sendMessage);

// ✅ Get all chat messages between 2 users (or with bot)
router.get("/:receiverId", authMiddleware.protect, getMessages);

module.exports = router;
