package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchResultBean {
    /**
     * records : [{"id":1,"curriculumName":"Java生产环境下性能监控与调优详解","curriculumImage":"img1.sycdn.imooc.com/szimg/5b384772000132c405400300-500-284.jpg","curriculumIntro":"Java生产环境下性能监控与调优详解\n本课程将为你讲解如何在生产环境下对Java应用做性能监控与调优；通过本课程，你将掌握多种性能监控工具应用，学会定位并解决诸如内存溢出、cpu负载飙高等问题；学会线上代码调试，Tomcat、Nginx，GC调优等手段； 读懂JVM字节码指令，分析源码背后原理，提升应对线上突发状况的能力","curriculumStatus":1,"priceStatus":1,"curriculumPrice":188,"vipPrice":178,"participantNum":3,"popularStatus":1,"freeStatus":1,"payStatus":0,"specialStatus":0,"chargeType":1},{"id":2,"curriculumName":"微服务架构核心20讲","curriculumImage":"https://static001.geekbang.org/resource/image/ec/c5/ec965e5797d52a3de8ba0bdc1c38e9c5.jpg","curriculumIntro":"互联网时代，企业在瞬息万变的市场赢得和保持竞争优势的核心在于持续创新。业界前沿互联网公司的实践表明，微服务架构 (Microservices Architecture)是企业应对业务复杂性，支持大规模持续创新行之有效的架构手段。\n\n微服务架构作为一种渐进式的演进架构，自提出以来便被互联网企业和传统企业所重视和采用。微服务架构所涉及的知识广泛，其学习曲线相对陡峭，其中架构落地、网关、监控等技术问题是常见的挑战。技术人员光靠自学摸索，通常需要耗费不少时间精力。由于微服务架构可操作学习的案例相对较少，尤其是大型业务微服务架构应用案例，所以大家对微服务架构理念虽然有一定理解，但是对微服务架构如何落地缺乏可靠的最佳实践经验。\n\n技术基础和平台工具易学，但架构思维和落地经验难建。一个合格的架构师除了最核心的技术理论基础之外，必须具备良好的架构视野和思维模式，以及通过技术与业务结合的落地实践所总结的行之有效的经验和方法论。\n\n本视频课程特邀请有十余年互联网分布式系统研发和架构经验的资深架构师杨波老师就\u201c微服务架构核心要点\u201d做深入浅出的总结与阐述，希望能帮助技术人员在微服务架构落地实践中提高效率，少走弯路。\n\n","curriculumStatus":1,"priceStatus":1,"curriculumPrice":99,"vipPrice":89,"participantNum":1,"popularStatus":0,"freeStatus":1,"payStatus":0,"specialStatus":0,"chargeType":1},{"id":3,"curriculumName":"ZooKeeper实战与源码剖析","curriculumImage":"https://static001.geekbang.org/resource/image/a1/21/a157a953f5362892a2a37efae7b21b21.jpg","curriculumIntro":"ZooKeeper 是一个开源的分布式协同服务系统，在业界的应用非常广泛，已经有十多年的历史。\n\n大多数技术人员都可以很快上手 ZooKeeper，但大都局限于基于现有的 ZooKeeper 协同服务示例做一些简单的定制。如果想具备为自己的业务场景设计 ZooKeeper 协同服务应用的能力，就需要深刻理解 ZooKeeper 的内部工作原理。\n\n因此，这门课程除了讲解常见应用场景下的ZooKeeper开发实战，还对它的深层机制以及核心源代码进行了详细剖析，帮助你更灵活地根据自己的业务场景对 ZooKeeper 进行个性化定制开发。","curriculumStatus":1,"priceStatus":1,"curriculumPrice":0,"vipPrice":0,"participantNum":0,"popularStatus":0,"freeStatus":1,"payStatus":0,"specialStatus":0,"chargeType":1}]
     * total : 3
     * size : 8
     * current : 1
     * orders : []
     * hitCount : false
     * searchCount : true
     * pages : 1
     */

    private int total;
    private int size;
    private int current;
    private boolean hitCount;
    private boolean searchCount;
    private int pages;
    private List<RecordsBean> records;
    private List<?> orders;

    @NoArgsConstructor
    @Data
    public static class RecordsBean {
        /**
         * id : 1
         * curriculumName : Java生产环境下性能监控与调优详解
         * curriculumImage : img1.sycdn.imooc.com/szimg/5b384772000132c405400300-500-284.jpg
         * curriculumIntro : Java生产环境下性能监控与调优详解
         本课程将为你讲解如何在生产环境下对Java应用做性能监控与调优；通过本课程，你将掌握多种性能监控工具应用，学会定位并解决诸如内存溢出、cpu负载飙高等问题；学会线上代码调试，Tomcat、Nginx，GC调优等手段； 读懂JVM字节码指令，分析源码背后原理，提升应对线上突发状况的能力
         * curriculumStatus : 1
         * priceStatus : 1
         * curriculumPrice : 188.0
         * vipPrice : 178.0
         * participantNum : 3
         * popularStatus : 1
         * freeStatus : 1
         * payStatus : 0
         * specialStatus : 0
         * chargeType : 1
         */

        private int id;
        private String curriculumName;
        private String curriculumImage;
        private String curriculumIntro;
        private int curriculumStatus;
        private int priceStatus;
        private double curriculumPrice;
        private double vipPrice;
        private int participantNum;
        private int popularStatus;
        private int freeStatus;
        private int payStatus;
        private int specialStatus;
        private int chargeType;
    }
}
