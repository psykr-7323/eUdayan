const Discussion = require("../models/discussion");
const { successResponse, errorResponse } = require("../utils/response");

// ------------------- STUDENT -------------------

// Add new discussion
const createDiscussion = async (req, res) => {
  try {
    const discussion = await Discussion.create({
      user: req.user.id,
      text: req.body.text,
      media: req.body.media || null,
    });
    return successResponse(res, "Discussion created", discussion, 201);
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to create discussion");
  }
};

// Get all discussions
const getDiscussions = async (req, res) => {
  try {
    const discussions = await Discussion.find()
      .populate("user", "name schoolInfo") // show name and school
      .populate("comments.user", "name schoolInfo")
      .populate("comments.replies.user", "name schoolInfo")
      .sort({ createdAt: -1 });

    return successResponse(res, "Discussions fetched", discussions);
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to fetch discussions");
  }
};

// Get single discussion
const getDiscussionById = async (req, res) => {
  try {
    const discussion = await Discussion.findById(req.params.id)
      .populate("user", "name schoolInfo") // show name and school
      .populate("comments.user", "name schoolInfo")
      .populate("comments.replies.user", "name schoolInfo");

    if (!discussion) {
      return errorResponse(res, "Discussion not found", 404);
    }

    return successResponse(res, "Discussion fetched", discussion);
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to fetch discussion");
  }
};

// Like / Unlike discussion
const toggleLike = async (req, res) => {
  try {
    const discussion = await Discussion.findById(req.params.id);
    if (!discussion) return errorResponse(res, "Discussion not found", 404);

    // Check if the user's ID is already in the likes array
    const alreadyLiked = discussion.likes.some(likeId => likeId.equals(req.user.id));

    if (alreadyLiked) {
      discussion.likes.pull(req.user.id); // Mongoose .pull() can remove by string id
    } else {
      discussion.likes.push(req.user.id);
    }
    await discussion.save();

    return successResponse(res, "Like toggled", { likes: discussion.likes.length });
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to toggle like");
  }
};

// Update discussion (only owner)
const updateDiscussion = async (req, res) => {
  try {
    const discussion = await Discussion.findById(req.params.id);
    if (!discussion) return errorResponse(res, "Discussion not found", 404);

    if (discussion.user.toString() !== req.user.id.toString()) {
      return errorResponse(res, "Not authorized", 403);
    }

    discussion.text = req.body.text || discussion.text;
    discussion.media = req.body.media || discussion.media;
    await discussion.save();

    return successResponse(res, "Discussion updated", discussion);
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to update discussion");
  }
};

// Add comment
const addComment = async (req, res) => {
  try {
    const discussion = await Discussion.findById(req.params.id);
    if (!discussion) return errorResponse(res, "Discussion not found", 404);

    discussion.comments.push({ user: req.user.id, text: req.body.text });
    await discussion.save();

    return successResponse(res, "Comment added", discussion.comments.pop(), 201);
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to add comment");
  }
};

// Update comment (only owner)
const updateComment = async (req, res) => {
  try {
    const discussion = await Discussion.findById(req.params.id);
    if (!discussion) return errorResponse(res, "Discussion not found", 404);

    const comment = discussion.comments.id(req.params.commentId);
    if (!comment) return errorResponse(res, "Comment not found", 404);

    if (comment.user.toString() !== req.user.id.toString()) {
      return errorResponse(res, "Not authorized", 403);
    }

    comment.text = req.body.text || comment.text;
    await discussion.save();

    return successResponse(res, "Comment updated", comment);
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to update comment");
  }
};

// Reply on comment
const addReply = async (req, res) => {
  try {
    const discussion = await Discussion.findById(req.params.id);
    if (!discussion) return errorResponse(res, "Discussion not found", 404);

    const comment = discussion.comments.id(req.params.commentId);
    if (!comment) return errorResponse(res, "Comment not found", 404);

    comment.replies.push({ user: req.user.id, text: req.body.text });
    await discussion.save();

    return successResponse(res, "Reply added", comment.replies.pop(), 201);
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to add reply");
  }
};

// Update reply (only owner)
const updateReply = async (req, res) => {
  try {
    const discussion = await Discussion.findById(req.params.id);
    if (!discussion) return errorResponse(res, "Discussion not found", 404);

    const comment = discussion.comments.id(req.params.commentId);
    if (!comment) return errorResponse(res, "Comment not found", 404);

    const reply = comment.replies.id(req.params.replyId);
    if (!reply) return errorResponse(res, "Reply not found", 404);

    if (reply.user.toString() !== req.user.id.toString()) {
      return errorResponse(res, "Not authorized", 403);
    }

    reply.text = req.body.text || reply.text;
    await discussion.save();

    return successResponse(res, "Reply updated", reply);
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to update reply");
  }
};

// ------------------- ADMIN -------------------
const deleteDiscussion = async (req, res) => {
  try {
    await Discussion.findByIdAndDelete(req.params.id);
    return successResponse(res, "Discussion deleted");
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to delete discussion");
  }
};

const deleteComment = async (req, res) => {
  try {
    const discussion = await Discussion.findById(req.params.id);
    if (!discussion) return errorResponse(res, "Discussion not found", 404);

    const comment = discussion.comments.id(req.params.commentId);
    if (!comment) {
      return errorResponse(res, "Comment not found", 404);
    }
    comment.remove();
    await discussion.save();

    return successResponse(res, "Comment deleted");
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to delete comment");
  }
};

const deleteReply = async (req, res) => {
  try {
    const discussion = await Discussion.findById(req.params.id);
    if (!discussion) return errorResponse(res, "Discussion not found", 404);

    const comment = discussion.comments.id(req.params.commentId);
    if (!comment) return errorResponse(res, "Comment not found", 404);

    const reply = comment.replies.id(req.params.replyId);
    if (!reply) {
      return errorResponse(res, "Reply not found", 404);
    }
    reply.remove();
    await discussion.save();

    return successResponse(res, "Reply deleted");
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to delete reply");
  }
};

module.exports = {
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
};
