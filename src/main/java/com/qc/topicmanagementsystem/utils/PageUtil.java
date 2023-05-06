package com.qc.topicmanagementsystem.utils;


import lombok.Data;

/**
 * 具体功能：告诉他每页显示几条数据，和一共有多少数据，还有显示第几页数据，它帮我们算出需要分多少页，每页的起始索引
 */
@Data
public class PageUtil {

    public final static int PAGECOUNT=5;//每页显示几条数据   3
    private int pageNumber;//显示第几页数据  1
    private int size;// 一共有多少条数据   3
    private int startIndex;//对应页面开始的索引 include
    private int endIndex;//对应页面结束的索引      不包含
    private int pageNums;//一共有多少页


    /**
     * 构造方法：需要三个参数,每页显示几条数据，一共有多少条数据，显示第几页数据
     * @param pageCount
     * @param size
     * @param pageNumber
     */
    public PageUtil(int pageCount,int size,int pageNumber)
    {
        //根据参数给对应属性赋值
        this.pageNumber=pageNumber;
        this.size=size;

        //帮我们算出需要分多少页，每页的起始索引
        this.pageNums=size%pageCount==0?size/pageCount:size/pageCount+1;
        this.startIndex=this.PAGECOUNT*(pageNumber-1);//0

        if(pageNumber==pageNums)//如果请求页码刚好是最后一页，那么结束索引要好好计算一下
        {
            this.endIndex=size;
        }else {
            this.endIndex=this.PAGECOUNT*pageNumber;
        }

        //处理pageNumber=0的情况
        if(pageNumber==0)
        {
            this.startIndex=0;
            this.endIndex=0;
        }
        this.startIndex = pageNumber;
        this.endIndex = pageCount;
    }

    public static void main(String[] args) {
        PageUtil pageUtil=new PageUtil(5, 13,2);//13页， 10-20
        System.out.println(pageUtil);
    }

}

