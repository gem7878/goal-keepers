'use client';

import Link from 'next/link';
import { notFound, useParams } from 'next/navigation';
import React, { useEffect, useState } from 'react';

export default function CreateGoalLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const params: { createGoalId: string } = useParams();

  const [createGoalId, setCreateGoalId] = useState(1);

  useEffect(() => {
    setCreateGoalId(parseInt(params.createGoalId));
  }, [params.createGoalId]);

  const createGoalList = ['이름', '이미지', '기간', '상세내용'];

  if (createGoalId > createGoalList.length) {
    notFound();
  }
  return (
    <section className="gk-primary-create-section">
      <nav className="w-16">
        <ul className="flex px-1 border border-orange-300	justify-around rounded-md	items-center">
          {createGoalList.map((_, index: number) => {
            if (createGoalId === index + 1) {
              return (
                <li key={index} className="text-orange-300 text-xs	">
                  {index + 1}
                </li>
              );
            } else {
              return (
                <li key={index} className="leading-4	">
                  <div className="rounded-md w-2 h-2 bg-orange-100	"></div>
                </li>
              );
            }
          })}
        </ul>
      </nav>
      {children}
    </section>
  );
}
