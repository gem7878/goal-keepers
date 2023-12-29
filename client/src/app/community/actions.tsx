'use server';

import { GET as PostGet } from '../api/board/all/route';
import { PUT, DELETE } from '@/app/api/board/post/route';
import { POST } from '@/app/api/board/post/like/route';

export const handleGetPostAll = async (getData: { pageNum: number }) => {
  return PostGet(getData)
    .then((response: any) => {
      if (response.statusCode === 200) {
        return JSON.parse(response.body).data;
      }
    })
    .catch((error) => console.log(error));
};

export const handlePutPost = async (postData: {
  title: string;
  content: string;
  goalId: number;
  postId: number;
}) => {
  return PUT(postData)
    .then((response: any) => {
      if (response.statusCode === 200) {
        return JSON.parse(response.body);
      }
    })
    .catch((error) => console.log(error));
};

export const handleDeletePost = async (postData: { postId: number }) => {
  return DELETE(postData)
    .then((response: any) => {
      if (response.statusCode === 200) {
        // console.log(response);
        return JSON.parse(response.body);
      }
    })
    .catch((error) => console.log(error));
};
export const handleLikePost = async (postId: number) => {
  return POST(postId)
    .then((response: any) => {
      if (response.statusCode === 200) {
        // console.log(response);
        return JSON.parse(response.body);
      }
    })
    .catch((error) => console.log(error));
};
