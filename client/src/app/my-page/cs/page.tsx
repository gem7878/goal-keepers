'use client';

import { CsModal, FAQBox, InquiryModal, Table } from '@/components';
import React, {
  useCallback,
  useEffect,
  useLayoutEffect,
  useRef,
  useState,
} from 'react';
import { createPortal } from 'react-dom';
import { handleGetFAQ, handleGetInquiryAll } from './actions';
import { useDispatch, useSelector } from 'react-redux';
import { selectRender, setStateInquiry } from '@/redux/renderSlice';

interface InquiryTypes {
  inquiryId: number;
  title: string;
  content: string;
  createdAt: string;
  answered: boolean;
}
interface FAQTypes {
  title: string;
  content: string;
}

const Cs = () => {
  const [isCsOpen, setCsOpen] = useState(false);
  const [isInquiryOpen, setInquiryOpen] = useState(false);
  const [portalElement, setPortalElement] = useState<Element | null>(null);
  const [selectData, setSelectData] = useState<InquiryTypes | null>(null);
  const [pageable, setPageable] = useState({
    pageNumber: 1,
    last: false,
  });
  const [more, setMore] = useState<boolean>(false);
  const [tabFocus, setTabFocus] = useState<number>(0);
  const [inquiryList, setInquiryList] = useState<InquiryTypes[]>([]);
  const [FAQList, setFAQList] = useState<FAQTypes[]>([]);

  const containerRef = useRef<any>(null);

  const reduxInquiryData = useSelector(selectRender);
  const dispatch = useDispatch();

  useEffect(() => {
    setPortalElement(document.getElementById('portal'));
  }, [isCsOpen, isInquiryOpen]);

  useEffect(() => {
    if (tabFocus === 0) {
      onGetFAQData(pageable.pageNumber);
    } else if (tabFocus === 1) {
      onGetInquiryData(pageable.pageNumber);
    }
  }, [tabFocus]);
  useEffect(() => {
    if (reduxInquiryData.inquiryBoolean === true) {
      onGetInquiryData(pageable.pageNumber);
      dispatch(setStateInquiry(false));
    }
  }, [reduxInquiryData.inquiryBoolean]);

  const onGetFAQData = async (pageNumber: number) => {
    const formData = {
      pageNum: pageNumber,
    };
    const response = await handleGetFAQ(formData);

    if (response.success) {
      if (more) {
        setFAQList((prevFAQData) => [...prevFAQData, ...response.data.content]);
      } else {
        setFAQList(response.data.content);
      }
      setMore(false);
      setPageable({
        pageNumber: response.data.pageable.pageNumber + 1,
        last: response.data.last,
      });
    }
  };

  const onGetInquiryData = async (pageNumber: number) => {
    const formData = {
      pageNum: pageNumber,
    };
    const response = await handleGetInquiryAll(formData);
    if (response.success) {
      if (more) {
        setInquiryList((prevInquiryData) => [
          ...prevInquiryData,
          ...response.data.content,
        ]);
      } else {
        setInquiryList(response.data.content);
      }
      setMore(false);
      setPageable({
        pageNumber: response.data.pageable.pageNumber + 1,
        last: response.data.last,
      });
    }
  };

  useEffect(() => {
    if (more) {
      handleCheckLastPage();
    }
  }, [more]);

  useLayoutEffect(() => {
    if (containerRef.current) {
      containerRef.current.addEventListener('scroll', handleScroll);
      // return () =>
      //   containerRef.current.removeEventListener('scroll', handleScroll);
    }
  }, [tabFocus]);

  const handleScroll = useCallback(() => {
    if (containerRef.current) {
      const elements = containerRef.current.querySelectorAll('.table-element');

      if (elements.length > 0) {
        const lastElement = elements[elements.length - 1];

        const lastComment = lastElement.getBoundingClientRect().bottom;
        const parentComment =
          lastElement.parentElement.getBoundingClientRect().bottom;

        if (lastComment - parentComment < 2) {
          setMore(true);
        }
      }
    }
  }, []);

  const handleCheckLastPage = () => {
    const pageNumber = pageable.pageNumber + 1;
    if (pageable.last) {
      console.log('마지막 페이지 입니다.');
      setMore(false);
    } else {
      tabFocus === 0 ? onGetFAQData(pageNumber) : onGetInquiryData(pageNumber);
    }
  };

  const onChangeTabFocus = (focus: number) => {
    setPageable({ pageNumber: 1, last: false });
    setTabFocus(focus);
  };

  return (
    <div className="w-5/6 flex flex-col h-[90%]">
      <h1 className="font-bold	text-xl	my-8 pl-4">고객센터</h1>
      <section className="w-full h-full text-white">
        <nav className="w-full h-10">
          <ul className="flex h-full">
            <li
              className={`w-1/2 ${
                tabFocus === 0
                  ? `bg-white border-x border-t border-orange-300 text-orange-500 `
                  : `bg-orange-100 border-b border-orange-300 text-orange-300 `
              } `}
            >
              <button
                className="w-full h-full pr-3"
                onClick={() => onChangeTabFocus(0)}
              >
                FAQ
              </button>
            </li>
            <li
              className={`w-1/2 ${
                tabFocus === 1
                  ? `bg-white border-x border-t border-orange-300 text-orange-500 `
                  : `bg-orange-100 border-b border-orange-300 text-orange-300 `
              } `}
            >
              <button
                className="w-full h-full pr-3 "
                onClick={() => onChangeTabFocus(1)}
              >
                나의 문의 내역
              </button>
            </li>
          </ul>
        </nav>
        {tabFocus === 0 ? (
          <div className="h-[calc(100%-60px)] pl-2 py-4">
            <ul className="h-full w-full overflow-y-auto" ref={containerRef}>
              {FAQList.map((data, index) => {
                return <FAQBox key={index} FAQData={data}></FAQBox>;
              })}
            </ul>
          </div>
        ) : (
          <>
            <div className="mt-5 text-neutral-600 h-[calc(100%-60px)] flex flex-col justify-between">
              <div
                className="h-[calc(100%-40px)] overflow-y-auto mb-2"
                ref={containerRef}
              >
                <Table
                  data={inquiryList}
                  setSelectData={setSelectData}
                  setOpen={setCsOpen}
                ></Table>
              </div>
              <button
                className="py-1 px-2 bg-orange-400 text-white text-sm"
                onClick={() => setInquiryOpen(true)}
              >
                문의하기
              </button>
            </div>
            {isCsOpen && portalElement
              ? createPortal(
                  <CsModal setOpen={setCsOpen} selectData={selectData} />,
                  portalElement,
                )
              : null}
            {isInquiryOpen && portalElement
              ? createPortal(
                  <InquiryModal
                    setOpen={setInquiryOpen}
                    setPageable={setPageable}
                  />,
                  portalElement,
                )
              : null}
          </>
        )}
      </section>
    </div>
  );
};

export default Cs;
