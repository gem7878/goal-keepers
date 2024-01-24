'use client';

import { CsModal, Table } from '@/components';
import React, { useEffect, useState } from 'react';
import { createPortal } from 'react-dom';

const Cs = () => {
  const [isOpen, setOpen] = useState(false);
  const [portalElement, setPortalElement] = useState<Element | null>(null);
  const [selectData, setSelectData] = useState<{
    index: 0;
    title: '';
    description: '';
    date: '';
  } | null>(null);

  useEffect(() => {
    setPortalElement(document.getElementById('portal'));
  }, [isOpen]);

  const columns = [
    { Header: '', accessor: 'index' },
    { Header: '제목', accessor: 'title' },
    { Header: '날짜', accessor: 'date' },
  ];

  const data = [
    { index: 1, title: '질문1', date: '2024-01-24', description: 'ㅇㅇㅇ' },
    { index: 2, title: '질문2', date: '2024-01-25', description: 'ㅇㅇㅇ' },
    { index: 3, title: '질문3', date: '2024-01-26', description: 'ㅇㅇㅇ' },
    { index: 4, title: '질문4', date: '2024-01-27', description: 'ㅇㅇㅇ' },
    { index: 5, title: '질문5', date: '2024-01-28', description: 'ㅇㅇㅇ' },
    // Add more data as needed
  ];
  return (
    <div className="w-5/6 flex flex-col h-3/5">
      <h1 className="font-bold	text-xl	mb-10 pl-4">고객센터</h1>
      <Table
        columns={columns}
        data={data}
        setSelectData={setSelectData}
        setOpen={setOpen}
      />
      {isOpen && portalElement
        ? createPortal(
            <CsModal setOpen={setOpen} selectData={selectData} />,
            portalElement,
          )
        : null}
    </div>
  );
};

export default Cs;
