'use server';

import { POST as EmailPOST } from '@/app/api/auth/email/route';
import { POST as NickNamePOST } from '@/app/api/auth/nickname/route';
import { POST as FormPOST } from '@/app/api/auth/signup/route';

export const handleConfirmEmail = async (email: string) => {
  const postData = {
    email: email,
  };
  return EmailPOST(postData)
    .then((response) => {
      if (response.statusCode === 200) {
        return JSON.parse(response.body);
      } else {
        console.log(response.body);
      }
    })
    .catch((error) => console.log(error));
};

export const handleConfirmNickName = async (nickname: string) => {
  const postData = {
    nickname: nickname,
  };
  return NickNamePOST(postData)
    .then((response) => {
      return JSON.parse(response.body);
    })
};

export const handleSubmitForm = async (formData: any) => {
  console.log(formData);

  const postData = {
    email: formData.email,
    password: formData.password,
    nickname: formData.nickname,
  };
  return FormPOST(postData)
    .then((response) => {
      if (response.statusCode === 200) {
        return JSON.parse(response.body);
      } else {
        console.log(response.body);
      }
    })
    .catch((error) => console.log(error));
};
