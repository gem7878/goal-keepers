'use server';

import { GET } from '@/app/api/board/all-comment/route';
import { POST, PUT, DELETE } from '@/app/api/board/comment/route';

export const handleGetComment = async (getData: {
  postId: number;
  page: number;
}) => {
  return GET(getData)
    .then((response: any) => {
      if (response.statusCode === 200) {
        return JSON.parse(response.body);
      }
    })
    .catch((error) => console.log(error));
};

export const handleCreateComment = async (formData: {
  postId: number;
  content: string;
}) => {
  return POST(formData)
    .then((response: any) => {
      if (response.statusCode === 200) {
        return JSON.parse(response.body);
      }
    })
    .catch((error) => console.log(error));
};

export const handleUpdateComment = async (formData: {
  commentId: number;
  content: string;
}) => {
  return PUT(formData)
    .then((response: any) => {
      if (response.statusCode === 200) {
        return JSON.parse(response.body);
      }
    })
    .catch((error) => console.log(error));
};

export const handleDeleteComment = async (formData: { commentId: number }) => {
  return DELETE(formData)
    .then((response: any) => {
      if (response.statusCode === 200) {
        return JSON.parse(response.body);
      }
    })
    .catch((error) => console.log(error));
};
