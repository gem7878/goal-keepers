"use client";

import { useParams } from "next/navigation";
import React, { useState } from "react";
import Datepicker, {
  DatepickerType,
  DateValueType,
} from "react-tailwindcss-datepicker";

const CreateGoal = ({ params }: { params: { createGoalId: string } }) => {
  const [date, setDate] = useState({
    startDate: null,
    endDate: null,
  });

  const handleValueChange = (newDate: any) => {
    setDate({
      startDate: newDate?.startDate,
      endDate: newDate?.endDate,
    });
  };

  return (
    <section className="h-96	w-full flex flex-col	items-center">
      {params.createGoalId === "1" ? (
        <>
          <h1 className="gk-primary-h1">목표의 이름을 설정하세요</h1>
          <input
            type="text"
            placeholder="이름을 입력하세요."
            className="border-b	w-full outline-none h-10"
            maxLength={24}
            autoFocus
          ></input>
        </>
      ) : params.createGoalId === "2" ? (
        <>
          <h1 className="gk-primary-h1">이미지를 선택하세요</h1>
          <div className="border w-full h-40">
            <input type="file" className="w-full"></input>
          </div>
        </>
      ) : params.createGoalId === "3" ? (
        <>
          <h1 className='gk-primary-h1'>기간을 설정하세요(선택)</h1>
          <Datepicker
            useRange={false}
            primaryColor={"orange"}
            value={date}
            onChange={handleValueChange}
            showShortcuts={false}
          ></Datepicker>
        </>
      ) : params.createGoalId === "4" ? (
        <>
          <h1 className='gk-primary-h1'>상세내용을 입력하세요(선택)</h1>
          <textarea
            className="border resize-none	w-full p-2 h-40 outline-none"
            placeholder="내용을 입력하세요."
            autoFocus
          ></textarea>
        </>
      ) : (
        <></>
      )}
    </section>
  );
};

export default CreateGoal;
