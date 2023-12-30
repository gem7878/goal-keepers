'use server';

import { POST } from '@/app/api/goal-list/goal/route';

export const handlePostGoalData = async (postData: {
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  imageUrl: string;
}) => {
  return POST(postData)
    .then((response) => {
      if (response.statusCode === 200) {
        return JSON.parse(response.body);
      } else {
        console.log(response.body);
      }
    })
    .catch((error) => console.log(error));
};
