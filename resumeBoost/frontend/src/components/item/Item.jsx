import axios from "axios";
import React, { useEffect, useState } from "react";
import { EC2_URL } from "../../constans";

const Item = () => {
  const [item, setItem] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [startPage, setStartPage] = useState(1);
  const [endPage, setEndPage] = useState(1);
  const [category, setCategory] = useState("");

  const itemFn = async (page) => {
    try {
      const rs = await axios.get(`http://${EC2_URL}:8090/item/itemList`, {
        params: {
          page,
          category: category,
        },
      });
      setStartPage(rs.data.startPage);
      setEndPage(rs.data.endPage);
      setItem(rs.data.itemList.content);
      setCurrentPage(page);
      setTotalPages(rs.data.itemList.totalPages);
      console.log(rs.data);
      console.log(item);
    } catch (err) {
      console.log(err);
    }
  };

  const categoryChangeFn = (e) => {
    setCategory(e);
    setCurrentPage(0);
  };

  useEffect(() => {
    itemFn(currentPage);
  }, [category, currentPage]);
  return (
    <>
      <div className="item-container">
        <div className="categories">
          <div onClick={() => categoryChangeFn("")} className="category">
            전체보기
          </div>
          <div onClick={() => categoryChangeFn("이력서")} className="category">
            이력서
          </div>
          <div
            onClick={() => categoryChangeFn("자기소개서")}
            className="category"
          >
            자기소개서
          </div>
          <div
            onClick={() => categoryChangeFn("면접컨설팅")}
            className="category"
          >
            면접컨설팅
          </div>
        </div>
        <div className="itemList">
          {item.length > 0 ? (
            item.map((el) => (
              <div key={el.id} className="item">
                <ul>
                  <li>{el.category}</li>
                  <li>{el.itemPrice}</li>
                </ul>
              </div>
            ))
          ) : (
            <p>상품 없음</p>
          )}
        </div>
        <div className="pagination">
          <button
            onClick={() => setCurrentPage(currentPage - 1)}
            disabled={currentPage == 0}
          >
            이전
          </button>
          <button
            onClick={() => setCurrentPage(currentPage + 1)}
            disabled={currentPage == totalPages - 1}
          >
            다음
          </button>
        </div>
      </div>
    </>
  );
};

export default Item;
