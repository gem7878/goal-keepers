import { useCallback, useEffect, useLayoutEffect, useState } from 'react';

export const pageScroll = (containerRef: any, pageable: any, func: any) => {
  const [more, setMore] = useState<boolean>(false);

  useEffect(() => {
    if (more) {
      handleCheckLastPage();
    }
  }, [more]);

  useLayoutEffect(() => {
    if (containerRef.current) {
      containerRef.current.addEventListener('scroll', handleScroll);
      return () =>
        containerRef.current.removeEventListener('scroll', handleScroll);
    }
  }, []);

  const handleScroll = useCallback(() => {
    if (containerRef.current) {
      const elements =
        containerRef.current.querySelectorAll('.comment-element');

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
    } else {
      func(pageNumber);
    }
  };
};
