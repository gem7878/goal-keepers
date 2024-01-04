'use client';

import { handleConfirmNickName } from '@/app/(auth)/register/actions';
import React, { useEffect, useState } from 'react';
import { Controller, SubmitHandler, useForm, useWatch } from 'react-hook-form';
import { handleChangeNickname, handleChangePassword } from './actions';
import { useSearchParams } from 'next/navigation';
interface IFormInput {
  email: string;
  exPassword: string;
  newPassword: string;
}
const Account = () => {
  const {
    control,
    getValues,
    setError,
    watch,
    handleSubmit,
    formState: { errors, isValid },
  } = useForm({
    mode: 'onChange',
    defaultValues: {
      email: '',
      exPassword: '',
      newPassword: '',
    },
  });
  const [focusBtn, setFocusBtn] = useState('');
  const [nicknameInput, setNicknameInput] = useState('');
  const [isCheckingDuplicate, setIsCheckingDuplicate] = useState(false);

  const PASSWORD_REGEX = /^(?=.*\d)(?=.*[a-z])(?=.*[!@#])[\da-zA-Z!@#]{8,20}$/;

  // 닉네임 중복 확인
  const handleConfirmDuplicationNickname = async () => {
    if (nicknameInput.length > 0) {
      await handleConfirmNickName(nicknameInput)
        .then((response) => {
          setIsCheckingDuplicate(response.success);
          alert(response.message);
        })
        .catch((error) => console.log(error));
    } else {
      alert('닉네임을 입력하세요.');
    }
  };
  const onUpdateNickname = async () => {
    if (isCheckingDuplicate === false) {
      alert('닉네임 중복 확인을 해주세요.');
    } else {
      if (nicknameInput.length > 0) {
        await handleChangeNickname(nicknameInput)
          .then((response) => {
            if (response.success) {
              alert('닉네임 변경 완료!');
            }
          })
          .catch((error) => console.log(error));
      } else {
        alert('닉네임을 입력하세요');
      }
    }
  };
  const onChangePassword = async () => {
    const emailValue = getValues('email');
    const exPwValue = getValues('exPassword');
    const newPwValue = getValues('newPassword');

    if (
      emailValue.length > 0 &&
      exPwValue.length > 0 &&
      newPwValue.length > 0
    ) {
      const formData = {
        email: emailValue,
        exPassword: exPwValue,
        newPassword: newPwValue,
      };
      await handleChangePassword(formData)
        .then((response) => {
          console.log(response);
          if (response.status === 400) {
            if (response.validation !== null) {
              alert(response.validation[0].message);
            } else {
              alert(response.message);
            }
          } else if (response.success === true) {
            alert('비밀번호가 변경되었습니다.');
          }
        })
        .catch((error) => console.log(error));
    } else {
      alert('빈 칸을 모두 채워주세요.');
    }
  };
  return (
    <div className="w-10/12 h-3/4 flex flex-col items-center justify-between">
      <h2 className="font-bold text-2xl">계정 관리</h2>
      <section className="w-full mb-10">
        <ul className="w-full flex flex-col gap-5">
          <li className="flex flex-col">
            <button
              className="gk-primary-login-button"
              onClick={() =>
                focusBtn === 'nickname'
                  ? setFocusBtn('')
                  : setFocusBtn('nickname')
              }
            >
              닉네임 변경
            </button>
            {focusBtn === 'nickname' && (
              <div className=" p-4 flex flex-col items-center">
                <div className="flex justify-between w-full mt-3">
                  <input
                    value={nicknameInput}
                    className="w-[calc(100%-52px)] border-b"
                    onChange={(e) => {
                      setNicknameInput(e.target.value);
                      if (e.target.value.length > 0) {
                        setIsCheckingDuplicate(false);
                      }
                    }}
                    placeholder="변경할 닉네임 입력 (15자 이하)"
                    maxLength={15}
                  ></input>
                  <button
                    className={`${
                      !isCheckingDuplicate ? 'text-orange-400' : 'text-gray-400'
                    } text-sm text-center w-14`}
                    type="button"
                    onClick={() => handleConfirmDuplicationNickname()}
                    disabled={isCheckingDuplicate}
                  >
                    중복 확인
                  </button>
                </div>
                <button
                  className={`w-20 h-10 border mt-4`}
                  onClick={() => onUpdateNickname()}
                  type="button"
                >
                  변경
                </button>
              </div>
            )}
          </li>
          <li>
            <button
              className="gk-primary-login-button"
              onClick={() =>
                focusBtn === 'password'
                  ? setFocusBtn('')
                  : setFocusBtn('password')
              }
            >
              비밀번호 변경
            </button>
            {focusBtn === 'password' && (
              <div className=" p-4 flex flex-col items-center">
                <div className="flex flex-col justify-between w-full mt-3">
                  <Controller
                    name="email"
                    control={control}
                    rules={{
                      required: true,
                    }}
                    render={({ field }) => (
                      <input
                        type="email"
                        className="w-full border-b"
                        {...field}
                        placeholder="현재 사용중인 이메일 입력하세요"
                      ></input>
                    )}
                  ></Controller>
                  <Controller
                    name="exPassword"
                    control={control}
                    rules={{
                      required: true,
                      pattern: PASSWORD_REGEX,
                    }}
                    render={({ field }) => (
                      <input
                        type="password"
                        className="w-full border-b mt-4"
                        {...field}
                        placeholder="현재 사용중인 비밀번호를 입력하세요"
                      ></input>
                    )}
                  ></Controller>
                  <Controller
                    name="newPassword"
                    control={control}
                    rules={{
                      required: true,
                      pattern: PASSWORD_REGEX,
                    }}
                    render={({ field }) => (
                      <input
                        type="password"
                        className="w-full border-b mt-4"
                        {...field}
                        placeholder="새로운 비밀번호를 입력하세요"
                      ></input>
                    )}
                  ></Controller>
                  <span className="text-red-600 text-xs">
                    {errors.newPassword?.type == 'pattern'
                      ? '* 소문자 영단어, 숫자, 특수문자를 모두 포함 8~20자'
                      : ''}
                  </span>
                </div>
                <input
                  className={`w-20 h-10 border mt-4`}
                  onClick={() => onChangePassword()}
                  type="submit"
                  value={'변경'}
                  disabled={isValid === false}
                ></input>
              </div>
            )}
          </li>
          <li>
            <button className="w-full text-center h-12 border rounded-md leading-9 text-sm text-gray-600">
              회원 탈퇴
            </button>
          </li>
        </ul>
      </section>
    </div>
  );
};

export default Account;
