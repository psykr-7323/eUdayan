const express = require("express");
const router = express.Router();
const {
  createDiscussion,
  getDiscussions,
  getDiscussionById,
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

// @route   GET & POST /api/discussions
router.route("/").get(getDiscussions).post(protect, createDiscussion);

// @route   GET, PUT & DELETE /api/discussions/:id
router
  .route("/:id")
  .get(getDiscussionById)
  .put(protect, updateDiscussion)
  .delete(protect, adminOnly, deleteDiscussion);

// @route   POST /api/discussions/:id/like
router.route("/:id/like").post(protect, toggleLike);

// @route   POST /api/discussions/:id/comments
router.route("/:id/comments").post(protect, addComment);

// @route   PUT & DELETE /api/discussions/:id/comments/:commentId
router
  .route("/:id/comments/:commentId")
  .put(protect, updateComment)
  .delete(protect, adminOnly, deleteComment);

// @route   POST /api/discussions/:id/comments/:commentId/replies
router.route("/:id/comments/:commentId/replies").post(protect, addReply);

// @route   PUT & DELETE /api/discussions/:id/comments/:commentId/replies/:replyId
router
  .route("/:id/comments/:commentId/replies/:replyId")
  .put(protect, updateReply)
  .delete(protect, adminOnly, deleteReply);

module.exports = router;