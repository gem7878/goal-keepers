'use server';

import { POST } from '@/app/api/goal-list/goal/route';

export const handlePostGoalData = async (formData: any) => {
  const postData = {
    formData: formData,
  };
  return POST(postData)
    .then((response) => {
      if (response?.statusCode === 200) {
        return { ok: true };
      } else {
        return { ok: false };
      }
    })
    .catch((error) => console.log(error));
};
