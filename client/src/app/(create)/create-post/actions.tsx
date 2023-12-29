'use server';

import { POST } from '@/app/api/board/post/route';

export const handleCreatePost = async (postData: {
  goalId: number;
  title: string;
  content: string;
}) => {
  return POST(postData)
    .then((reponse) => {
      if (reponse.statusCode === 200) {
        return JSON.parse(reponse.body);
      }
    })
    .catch((error) => console.log(error));
};
