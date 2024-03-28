'use client';

import React, { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCaretUp, faCaretDown } from '@fortawesome/free-solid-svg-icons';

const FAQBox: React.FC<{ FAQData: { title: string; content: string } }> = ({
  FAQData,
}) => {
  const [isFocus, setIsFocus] = useState(false);
  return (
    <li className="w-[calc(100%-8px)] table-element mt-3 text-neutral-600 text-[15px]">
      <div
        className="bg-orange-100 flex justify-between p-4 rounded-xl"
        onClick={() => setIsFocus(!isFocus)}
      >
        <h3 className="w-[calc(100%-12px)]">{FAQData.title}</h3>
        <button className="w-3">
          <FontAwesomeIcon
            icon={isFocus ? faCaretUp : faCaretDown}
          ></FontAwesomeIcon>
        </button>
      </div>
      {isFocus && (
        <div className="p-5 text-sm text-neutral-500">
          <p>{FAQData.content}</p>
        </div>
      )}
    </li>
  );
};

export default FAQBox;
