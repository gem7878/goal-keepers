'use client';

import React, { SetStateAction } from 'react';

interface InquiryTypes {
  inquiryId: number;
  title: string;
  content: string;
  createdAt: string;
  answered: boolean;
}

interface TableTypes {
  data: InquiryTypes[];
  setSelectData: React.Dispatch<SetStateAction<InquiryTypes | null>>;
  setOpen: React.Dispatch<SetStateAction<boolean>>;
}

const Table: React.FC<TableTypes> = ({ data, setSelectData, setOpen }) => {
  return (
    <div className="w-full table-element">
      <header className="w-full">
        <ul className="w-full flex border-b border-neutral-300 bg-neutral-100 p-1 text-center text-sm">
          <li className="w-[58%]">제목</li>
          <li className="w-[29%]">날짜</li>
          <li className="w-[13%]">답변</li>
        </ul>
      </header>
      <main className="w-full">
        <ul className="w-full">
          {data.map((value, i) => {
            return (
              <li
                key={i}
                className="w-full border-b border-neutral-300 flex text-[13px] text-center py-5 "
                onClick={() => {
                  setSelectData(value);
                  setOpen(true);
                }}
              >
                <div className="w-[58%] ">
                  <span>
                    {value.title.length > 14
                      ? value.title.slice(0, 14) + '...'
                      : value.title}
                  </span>
                </div>
                <div className="w-[29%]">
                  <span>{value.createdAt.slice(0, 10)}</span>
                </div>
                <div className="w-[13%]">
                  <span
                    className={`${
                      value.answered ? 'text-red-600' : 'text-gray-400'
                    }`}
                  >
                    {value.answered ? '완료' : '대기중'}
                  </span>
                </div>
              </li>
            );
          })}
        </ul>
      </main>
    </div>
  );
};

export default Table;
