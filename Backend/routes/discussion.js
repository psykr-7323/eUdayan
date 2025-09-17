const express = require("express");
const {
  createDiscussion,
  getDiscussions,
  toggleLike,
  updateDiscussion,
  addComment,
  updateComment,
  addReply,
  updateReply,
  deleteDiscussion,
  deleteComment,
  deleteReply,
} = require("../controllers/discussionController");
const { protect, adminOnly } = require("../middleware/authMiddleware");

const router = express.Router();

// Student
router.post("/", protect, createDiscussion);
router.get("/", protect, getDiscussions);
router.post("/:id/like", protect, toggleLike);
router.put("/:id", protect, updateDiscussion);

router.post("/:id/comment", protect, addComment);
router.put("/:id/comment/:commentId", protect, updateComment);

router.post("/:id/comment/:commentId/reply", protect, addReply);
router.put("/:id/comment/:commentId/reply/:replyId", protect, updateReply);

// Admin
router.delete("/:id", protect, adminOnly, deleteDiscussion);
router.delete("/:id/comment/:commentId", protect, adminOnly, deleteComment);
router.delete("/:id/comment/:commentId/reply/:replyId", protect, adminOnly, deleteReply);

module.exports = router;
