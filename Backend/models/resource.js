const mongoose = require("mongoose");

const feedbackSchema = new mongoose.Schema(
  {
    student: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User", // student reference
      required: true,
    },
    comment: {
      type: String,
      required: true,
    },
    rating: {
      type: Number,
      min: 1,
      max: 5,
      default: 3,
    },
  },
  { timestamps: true }
);

const resourceSchema = new mongoose.Schema(
  {
    title: {
      type: String,
      required: true,
    },
    type: {
      type: String,
      enum: ["video", "audio", "article"],
      required: true,
    },
    url: {
      type: String,
      required: true,
    },
    category: {
      type: String,
      default: "general",
    },
    feedback: [feedbackSchema], // student feedback array
  },
  { timestamps: true }
);

module.exports = mongoose.model("Resource", resourceSchema);
