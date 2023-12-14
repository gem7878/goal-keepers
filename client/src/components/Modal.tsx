"use client";

import Image, { StaticImageData } from "next/image";
import React, { SetStateAction, useRef, useState } from "react";

interface selectDataTypes {
  image: any;
  goalTitle: string;
  goalContent: string;
  startDate: string;
  endDate: string;
  goalComment: string[];
}

const Modal: React.FC<{
  setOpen: React.Dispatch<SetStateAction<boolean>>;
  selectData: selectDataTypes | null;
  setSelectGoalNum: React.Dispatch<SetStateAction<number | null>>;
}> = ({ setOpen, selectData, setSelectGoalNum }) => {
  const [isEdit, setIsEdit] = useState<boolean>(false);
  const containerRef = useRef<HTMLElement>(null);
  const handleOutsideClick = (e: any) => {
    if (!containerRef.current?.contains(e.target)) {
      setOpen(false);
      setSelectGoalNum(null);
    }
  };
  const handleConfirmButton = () => {
    if (isEdit) {
      setIsEdit(false);
    } else {
      setIsEdit(true);
      setOpen(false);
      setSelectGoalNum(null);
    }
  };
  return (
    <div
      className="fixed top-0 w-screen h-screen bg-black bg-opacity-70 flex items-center justify-center"
      onClick={(e) => handleOutsideClick(e)}
    >
      <main className="w-3/4 h-3/5 bg-white opacity-100 " ref={containerRef}>
        <section className="w-full h-1/5 relative">
          <Image
            src={selectData?.image}
            alt=""
            style={{ width: "100%", height: "100%", objectFit: "cover" }}
          ></Image>
          <div className="absolute top-0 w-full h-full bg-opacity-50 bg-black flex items-center">
            <h2 className="text-white ml-8 text-base font-bold">
              {selectData?.goalTitle}
            </h2>
          </div>
          {!isEdit && (
            <button
              className="absolute text-white text-xs bottom-1 right-2"
              onClick={() => setIsEdit(true)}
            >
              edit
            </button>
          )}
        </section>
        <section className="h-3/5 w-full pt-8 px-8 flex flex-col justify-between">
          <div className="w-full">
            <textarea
              value={selectData?.goalContent}
              readOnly={isEdit ? false : true}
            ></textarea>
          </div>
          <span className="w-full text-xs">
            {selectData?.startDate} ~ {selectData?.endDate}
          </span>
        </section>
        <section className="h-1/5 w-full flex items-center justify-center">
          <button
            className="w-1/4 bg-orange-300 rounded-lg h-1/3 text-white"
            onClick={() => handleConfirmButton()}
          >
            {isEdit ? "수정" : "확인"}
          </button>
        </section>
      </main>
    </div>
  );
};

export default Modal;
