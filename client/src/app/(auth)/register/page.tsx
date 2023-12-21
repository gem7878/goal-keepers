"use client";

import React, { useEffect, useState } from "react";
import {
  handleConfirmEmail,
  handleConfirmNickName,
  handleSubmitForm,
} from "./actions";
import shortid from "shortid";
import { useRouter } from "next/navigation";

const Register = () => {
  const [form, setForm] = useState({
    email: "",
    validEmail: false,
    nonEmailDuplication: false,
    pw: "",
    validPw: "",
    pwCheck: "",
    correctPwCheck: false,
    nickname: "",
    nonNicknameDuplication: false,
  });
  const [message, setMessage] = useState({
    email: "",
    pw: "",
    pwCheck: "",
    nickname: "",
  });
  const [messageGreen, setMessageGreen] = useState({
    email: false,
    pw: false,
    pwCheck: false,
    nickname: false,
  });

  const router = useRouter();

  const test = shortid.generate();
  const messageList = [
    "* 이메일 중복을 확인해주세요",
    "* 이메일을 형식에 맞게 입력해주세요.",
    "* 비밀번호를 형식에 맞게 입력해주세요.",
  ];

  useEffect(() => {
    if (form.pwCheck.length > 0) {
      form.correctPwCheck
        ? setMessage({ ...message, pwCheck: "" })
        : setMessage({
            ...message,
            pwCheck: "* 비밀번호가 일치하지 않습니다.",
          });
      setMessageGreen({ ...messageGreen, pwCheck: false });
    }
  }, [form.correctPwCheck, form.pwCheck]);

  const hadleInputChange = (
    type: string,
    e: React.FormEvent<HTMLInputElement>
  ) => {
    if (type === "email") {
      setForm({
        ...form,
        email: e.currentTarget.value,
        nonEmailDuplication: false,
      });
    } else if (type === "pw") {
      setForm({
        ...form,
        pw: e.currentTarget.value,
        correctPwCheck: e.currentTarget.value === form.pwCheck,
      });
    } else if (type === "pwCheck") {
      setForm({
        ...form,
        pwCheck: e.currentTarget.value,
        correctPwCheck: e.currentTarget.value === form.pw,
      });
    } else if (type === "nickname") {
      setForm({
        ...form,
        nickname: e.currentTarget.value,
        nonNicknameDuplication: false,
      });
    }
  };

  const handleConfirmDuplicationEmail = async () => {
    await handleConfirmEmail(form.email)
      .then((response) => {
        setMessage({ ...message, email: `* ${response.message}` });
        setMessageGreen({ ...messageGreen, email: response.success });
        setForm({ ...form, nonEmailDuplication: true });
      })
      .catch((error) => console.log(error));
  };
  const handleConfirmDuplicationNickname = async () => {
    await handleConfirmNickName(form.nickname)
      .then((response) => {
        setMessage({ ...message, nickname: `* ${response.message}` });
        setMessageGreen({ ...messageGreen, nickname: response.success });
        setForm({ ...form, nonNicknameDuplication: true });
      })
      .catch((error) => console.log(error));
  };
  const hadleConfirmForm = async () => {
    if (
      form.email &&
      // form.validEmail &&
      form.nonEmailDuplication &&
      form.pw &&
      // form.validPw &&
      form.pwCheck &&
      form.correctPwCheck &&
      form.nonNicknameDuplication &&
      form.nickname
    ) {
      await handleSubmitForm({
        email: form.email,
        password: form.pw,
        nickname: form.nickname,
      })
        .then((response) => {
          alert(`${response.nickname}님! 회원가입이 완료되었습니다.`);
          return router.push("/login");
        })
        .catch((error) => console.log(error));
    } else {
      alert("다시 확인해주세요.");
    }
  };

  return (
    <>
      <h2 className="w-full text-center text-xl mt-20 font-bold">회원가입</h2>
      <form
        action={hadleConfirmForm}
        className="flex flex-col w-full gap-4"
        id="register"
      >
        <li className="flex flex-col w-full">
          <div className="flex items-end">
            <label htmlFor="email_input" className="text-sm">
              이메일
            </label>
            <p
              className={`${
                messageGreen.email ? "text-green-600" : "text-red-600"
              } text-xs ml-2`}
            >
              {message.email}
            </p>
          </div>
          <div className="border-b w-full flex justify-between p-3">
            <input
              id="email_input"
              type="email"
              placeholder="이메일을 입력하세요"
              className="w-[calc(100%-52px)]"
              value={form.email}
              onChange={(e) => hadleInputChange("email", e)}
            ></input>
            <button
              className={`${
                form.nonEmailDuplication ? "text-gray-400" : "text-orange-400"
              } text-sm text-center w-14`}
              type="button"
              onClick={() => {
                if (form.email.length > 0) {
                  handleConfirmDuplicationEmail();
                } else {
                  setMessage({ ...message, email: "* 이메일을 입력하세요" });
                  setMessageGreen({ ...messageGreen, email: false });
                }
              }}
            >
              중복확인
            </button>
          </div>
        </li>
        <li className="flex flex-col w-full">
          <div className="flex items-end">
            <label htmlFor="password_input" className="text-sm">
              비밀번호
            </label>
            <p className="text-red-600 text-xs ml-2">{message.pw}</p>
          </div>
          <div className="border-b w-full flex justify-between p-3">
            <input
              id="password_input"
              type="password"
              className="w-full"
              placeholder="비밀번호를 입력하세요"
              value={form.pw}
              onChange={(e) => hadleInputChange("pw", e)}
            ></input>
          </div>
        </li>
        <li className="flex flex-col w-full">
          <div className="flex items-end">
            <label htmlFor="password-check_input" className="text-sm">
              비밀번호 재확인
            </label>
            <p className="text-red-600 text-xs ml-2">{message.pwCheck}</p>
          </div>
          <div className="border-b w-full flex justify-between p-3">
            <input
              id="password-check_input"
              type="password"
              className="w-full"
              placeholder="비밀번호를 입력하세요"
              value={form.pwCheck}
              onChange={(e) => hadleInputChange("pwCheck", e)}
            ></input>
          </div>
        </li>
        <li className="flex flex-col w-full">
          <div className="flex items-end">
            <label htmlFor="nickname_input" className="text-sm">
              닉네임
            </label>
            <p
              className={`${
                messageGreen.nickname ? "text-green-600" : "text-red-600"
              } text-xs ml-2`}
            >
              {message.nickname}
            </p>
          </div>
          <div className="border-b w-full flex justify-between p-3">
            <input
              id="nickname_input"
              type="text"
              className="w-[calc(100%-52px)]"
              placeholder="닉네임을 입력하세요(15자 이하)"
              value={form.nickname}
              onChange={(e) => hadleInputChange("nickname", e)}
            ></input>
            <button
              className={`${
                form.nonNicknameDuplication
                  ? "text-gray-400"
                  : "text-orange-400"
              } text-sm text-center w-14`}
              type="button"
              onClick={() => {
                if (form.nickname.length > 0) {
                  handleConfirmDuplicationNickname();
                } else {
                  setMessage({
                    ...message,
                    nickname: "* 닉네임을 입력하세요",
                  });
                  setMessageGreen({ ...messageGreen, nickname: false });
                }
              }}
            >
              중복확인
            </button>
          </div>
        </li>
      </form>
      <input
        type="submit"
        value={"완료"}
        className={`gk-primary-login-button`}
        disabled={
          !(
            form.email.length > 0 &&
            form.pw.length > 0 &&
            form.pwCheck.length > 0 &&
            form.nickname.length > 0 &&
            form.correctPwCheck &&
            form.nonEmailDuplication &&
            form.nonNicknameDuplication
          )
          // !(
          //   form.validEmail &&
          //   form.nonEmailDuplication &&
          //   form.validPw &&
          //   form.correctPwCheck &&
          //   form.nonNicknameDuplication
          // )
        }
        form="register"
      ></input>
    </>
  );
};

export default Register;
