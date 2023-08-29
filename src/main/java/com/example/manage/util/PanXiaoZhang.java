package com.example.manage.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.data_statistics.Personnel;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.util.entity.*;
import com.example.manage.util.wechat.WechatMsg;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.Boolean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;

/**
 * @avthor 潘小章
 * @date 2021/12/16
 */

@Slf4j
public class PanXiaoZhang {
    private static final String token = "1";
    public static String ran(Integer integer){
        //随机数创建
        StringBuilder str=new StringBuilder();//定义变长字符串
        Random random=new Random();
        //随机生成数字，并添加到字符串
        for(int i=0;i<integer;i++){
            str.append(random.nextInt(10));
        }
        //将字符串转换为数字并输出
        Integer num=Integer.parseInt(str.toString());
        return num.toString();
    }
    public static byte[] hmacSHA256(byte[] data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data);
    }

    public static JSONObject xml(String base64Xml) {
        JSONObject jsonObject = new JSONObject();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(base64Xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = doc.getRootElement();// 指向根节点

        Iterator it = root.elementIterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();// 一个Item节点
            jsonObject.put(element.getName(),element.getTextTrim());
            //System.out.println(element.getName() + " : " + element.getTextTrim());
        }
        return jsonObject;
    }

    public static String getOrder(Date date) {
        String yyyyMMddHHmmss = DateFormatUtils.format(date, "yyyyMMddHHmmss");
        String s = yyyyMMddHHmmss + ran(8);
        return s;
    }

    public static List<String> riqi(Integer tianshu){
        //获取当前日期
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        Date today = new Date();
        String endDate = sdf .format( today ); //当前日期
        //获取三十天前日期
        Calendar theCa = Calendar. getInstance ();
        List<String> datefor30List = new ArrayList<>();
        for (int i = tianshu * -1;i < 1;i++){
            theCa .setTime( today );
            theCa .add(Calendar. DATE, i); //最后一个数字30可改，30天的意思
            Date start = theCa .getTime();
            String startDate = sdf.format( start ); //三十天之前日期
            datefor30List.add(startDate);
        }
        return datefor30List;
    }
    public static String intRiQi(Integer integer){
        //获取当前日期
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        Date today = new Date();
        String endDate = sdf .format( today ); //当前日期
        //获取三十天前日期
        Calendar theCa = Calendar. getInstance ();
        theCa.setTime( today );
        theCa.add(Calendar. DATE, integer); //最后一个数字30可改，30天的意思
        Date start = theCa .getTime();
        String startDate = sdf.format( start ); //三十天之前日期
        return startDate+" 00:00:00";
    }
    public static void addTxt(String fileName,String data){
        File file = new File(fileName); // 相对路径，如果没有则要建立一个新的output.txt文件
        try {
            FileOutputStream fos = new FileOutputStream(file,true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(data);
            bw.newLine();
            bw.flush();
            bw.close();
            osw.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 创建日期:2022年02月28日<br/>
     * 代码创建:黄聪and潘国豪<br/>
     * 功能描述:获取接受到的JSON数据<br/>
     * @param request
     * @return
     */
    public static  <T> T getJSONParam(HttpServletRequest request, Class<T> eClass) throws IOException {
        String requestURI = request.getRequestURI();
        log.info("接口:{}",requestURI);
        // 获取输入流
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        // 写入数据到Stringbuilder
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = streamReader.readLine()) != null) {
            sb.append(line);
        }
        //WebSocketServer.sendInfo(
        //        token,
        //        sb.toString());
        log.info("请求内容:{}",sb.toString());
        T t = JSONObject.parseObject(sb.toString(), eClass);
        // 直接将json信息打印出来
        return t;
    }
    /**
     * 获取request里的JSON数据
     *
     * @param request HttpServletRequest对象
     * @return JsonNode对象
     * @throws IOException
     */
    public static JsonNode getRequestJson(HttpServletRequest request) throws IOException {
        String requestURI = request.getRequestURI();
        log.info("接口:{}",requestURI);
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String requestData = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(requestData);
    }
    /**
     * 创建日期:2022年02月28日<br/>
     * 代码创建:黄聪and潘国豪<br/>
     * 功能描述:获取接受到的JSON数据<br/>
     * @param request
     * @return
     */
    public static Map getJsonMap(HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.info("接口:{}",requestURI);
        Map jsonToMap = new HashMap();
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            // 写入数据到Stringbuilder
            StringBuilder sb = new StringBuilder();
            Object line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            if (sb.toString().length() > 1){
                jsonToMap = JSONObject.parseObject(sb.toString());
            }
            // 直接将json信息打印出来
            log.info("请求内容:{}",sb.toString());
        } catch (Exception e) {
            log.info("99行异常");
        }
        //WebSocketServer.sendInfo(
        //        token,
        //        JSON.toJSONString(subjoinValue(jsonToMap)));
        return subjoinValue(jsonToMap,request);
    }
    public static Map subjoinValue(Map<String,Integer> map,HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.info("接口:{}",requestURI);
        Integer index = map.get("index");
        Integer pageNum = map.get("pageNum");
        if (ObjectUtils.isEmpty(pageNum)){
            map.put("pageNum",10);
        }else {
            if (Integer.valueOf(pageNum.toString()) > 100){
                map.put("pageNum",100);
            }else if (pageNum < 1){
                map.put("pageNum",10);
            }
        }
        if (ObjectUtils.isEmpty(index)){
            map.put("index",0);
        }else {
            if (index < 1){
                index = 1;
            }
            map.put("index",(Integer.valueOf(index.toString()) - 1) * map.get("pageNum"));
        }
        return map;
    }
    /**
     * 不带数据的
     * 创建日期:2022年02月28日<br/>
     * 代码创建:黄聪and潘国豪<br/>
     * 功能描述:获取接受到的JSON数据<br/>
     * @param request
     * @return
     */
    public static Map getMap(HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.info("接口:{}",requestURI);
        Map jsonToMap = new HashMap();
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            // 写入数据到Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            if (sb.toString().length() > 1){
                log.info(sb.toString());
                jsonToMap = JSONObject.parseObject(sb.toString());
            }
            // 直接将json信息打印出来
        } catch (Exception e) {
            log.info("99行异常");
        }
        return jsonToMap;
    }
    /**
     * 创建日期:2022年02月28日<br/>
     * 代码创建:黄聪and潘国豪<br/>
     * 功能描述:获取接受到的JSON数据<br/>
     * @param request
     * @return
     */
    public static String getJSONParamTwo(HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.info("接口:{}",requestURI);
        String str = null;
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            // 写入数据到Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            str = sb.toString();
            // 直接将json信息打印出来
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
    public static Boolean isInteger(String str){
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    public static String getJsonReturn(ReturnEntity returnEntity){
        return JSON.toJSONString(returnEntity);
    }
    //密码加密
    public static String getPassword(String password){
        try {
            return DigestUtils.md5DigestAsHex((password+"zhangxun123").getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            return password;
        }
    }
    /**
     * 计算x占y的百分比
     * @param x
     * @param y
     * @return
     */
    public static int percent(int x, int y) {
        if (x != 0 && y != 0){
            Double d_x = x * 1.0;
            Double d_y = y * 1.0;
            double number = (d_x / d_y) * 100;
            return (int) number;
        }
        return 0;
    }
    /**
     * 计算x占y的百分比
     * @param x
     * @param y
     * @return
     */
    public static double percent(int x, int y,Integer integer) {
        if (x != 0 && y != 0){
            Double d_x = x * 1.0;
            Double d_y = y * 1.0;
            double number = d_x / d_y;
            return doubleD(number,integer);
        }
        return 0D;
    }
    /**
     * 计算x占y的百分比
     * @param x
     * @param y
     * @return
     */
    public static double percent(int x, int y,Integer integer,Integer numberBai) {
        if (x != 0 && y != 0){
            Double d_x = x * 1.0;
            Double d_y = y * 1.0;
            double number = (d_x / d_y) * numberBai;
            return doubleD(number,integer);
        }
        return 0D;
    }

    //判断是否为空
    public static Boolean isNull(Object object){
        return null == object;
    }
    //判断是否有值
    public static Boolean isValueNull(Object object){
        return object != null && String.valueOf(object).replaceAll("\\s*", "").length() > 0;
    }
    //判断是否仅含有数字和字母
    public static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }
    //判断是否数字和字母组合
    public static Boolean isNANDl(String str){
        String reg="^[a-zA-Z0-9]{6,16}$";
        return str.matches(reg);
    }
    //去除特殊符号
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            dest = str.replaceAll("\\s*|\t|\r|\n|","");
        }
        return dest;
    }
    //判断是否符合账号
    public static boolean isAccount(String str){
        return !isNull(str) && isLetterDigit(str) && str.length() < 17 && str.length() > 5;
    }
    //判断是否符合密码
    public static boolean isPassword(String str){
        return !isNull(str) && isLetterDigit(str) && str.length() < 17 && str.length() > 5;
    }
    //针对token是否过期进行判断
    public static Integer tokenExpiration(Date addData,Date remove){
        Integer oldDateComparison = new Date().compareTo(new Date(addData.getTime() + 7200000));
        Integer DateComparison = new Date().compareTo(remove);
        if (DateComparison != -1){
            return 2;
        }
        if (oldDateComparison != -1){
            return 1;
        }
        return 0;
    }
    public static Boolean pureNumber(Object o){
        boolean result = o.toString().matches("[0-9]+");
        return result;
    }
    //接口token的数据
    public static String judgmentToken(HttpServletRequest request){
        String token = request.getHeader("token");
        TokenEntity tokenEntity = TokenUtil.tokenToOut(token);
        Integer integer = PanXiaoZhang.tokenExpiration(tokenEntity.getUserAddTime(), tokenEntity.getUserRemove());
        if (integer == 0){
            return token;
        }else {
            Map map = new HashMap();
            map.put("id",tokenEntity.getId());
            map.put("username",tokenEntity.getUserName());
            map.put("role",tokenEntity.getUserRole());
            String getoken = TokenUtil.getoken(map);
            log.info(tokenEntity.getId()+":"+tokenEntity.getUserName()+"token老化,更新为"+getoken);
            return getoken;
        }
    }
    /**
     *
     * @description: 实体类转Map
     * @author: Jeff
     * @date: 2019年10月29日
     * @param object
     * @return
     */
    public static Map<String, Object> entityToMap(Object object) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            try {
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(object);
                map.put(field.getName(), o);
                field.setAccessible(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    /**
     *
     * @description: Map转实体类
     * @author: Jeff
     * @date: 2019年10月29日
     * @param <T>
     * @param map    需要初始化的数据，key字段必须与实体类的成员名字一样，否则赋值为空
     * @param entity 需要转化成的实体类
     * @return
     */
    public static <T> T mapToEntity(Map<String, Object> map, Class<T> entity) {
        T t = null;
        try {
            t = entity.newInstance();
            for (Field field : entity.getDeclaredFields()) {
                if (map.containsKey(field.getName())) {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    Object object = map.get(field.getName());
                    if (object != null && field.getType().isAssignableFrom(object.getClass())) {
                        field.set(t, object);
                    }
                    field.setAccessible(flag);
                }
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }
    public static Double doubleDigit(Double d,Integer integer){
        return new BigDecimal(d).setScale(integer,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static Double doubleD(Double d,Integer integer){
        if (d > 0D){
            return new BigDecimal(d).setScale(integer,BigDecimal.ROUND_HALF_UP).doubleValue();
        }else {
            return d;
        }
    }
    public static String getSession(HttpServletRequest request,String name){
        return String.valueOf(request.getSession().getAttribute(name));
    }
    public static String yMdHms(){
        return "yyyy-MM-dd HH:mm:ss";
    }
    public static String Hms(){
        return "HH:mm:ss";
    }
    public static String yMd(){
        return "yyyy-MM-dd";
    }
    public static String yMd(Integer integer){
        return "yyyyMMdd";
    }
    public static Boolean timeVerify(String time){
        return isLegalDate(time.length(),time,yMdHms());
    }
    /**
     * 根据时间 和时间格式 校验是否正确
     * @param length 校验的长度
     * @param sDate 校验的日期
     * @param format 校验的格式
     * @return
     */
    public static Boolean isLegalDate(int length, String sDate,String format) {
        int legalLen = length;
        if ((sDate == null) || (sDate.length() != legalLen)) {
            return false;
        }
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }
    public static Integer compareTime(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(yMdHms());
        Date parse = sdf.parse(str);
        return parse.compareTo(new Date());
    }
    //更新时间
    public static Boolean forThreeValues(String one, String two, String there){
        if (
            PanXiaoZhang.isNull(one)
            || PanXiaoZhang.isNull(two)
            || PanXiaoZhang.isNull(there)
        ){
            return false;
        }
        if (one.equals("0") && two.length() > 0 && there.length() > 0){
            return true;
        }
        if (one.equals("1") && two.length() > 0 && there.length() > 0){
            return true;
        }
        return one.equals("2") && there.length() > 0;
    }
    //背景
    public static Boolean DetermineTheBackground(String home,String css,String img){
        if (home.equals("0")){
            return true;
        }
        if (home.equals("1") && !isNull(css) && css.length() > 0){
            return true;
        }
        if (home.equals("2") && !isNull(img) && img.length() > 0){
            return true;
        }
        return home.equals("3") && !isNull(css) && css.length() > 0 && !isNull(img) && img.length() > 0;
    }
    /**
     *
     * 描述:获取下一个月.
     *
     * @return
     */
    public static String getPreMonth(Integer amount) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, amount);
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
        String preMonth = dft.format(cal.getTime());
        return preMonth;
    }
    public static int getMonthDays(String yearMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(yearMonth));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Date dateToWeek(Date date,Integer week) {
        //获得入参的日期
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);

        // 获得入参日期是一周的第几天
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        // 获得入参日期相对于下周一的偏移量（在国外，星期一是一周的第二天，所以下周一是这周的第九天）
        // 若入参日期是周日，它的下周一偏移量是1
        Integer nextMondayOffset;
        if (week == 0){
            nextMondayOffset = 9 - dayOfWeek;
        }else if (week == 1){
            nextMondayOffset = 10 - dayOfWeek;
        }else if (week == 2){
            nextMondayOffset = 11 - dayOfWeek;
        }else if (week == 3){
            nextMondayOffset = 12 - dayOfWeek;
        }else if (week == 4){
            nextMondayOffset = 13 - dayOfWeek;
        }else if (week == 5){
            nextMondayOffset = 14 - dayOfWeek;
        }else {
            nextMondayOffset = 15 - dayOfWeek;
        }
        // 设置不同的日期只要使用不同的偏移量即可
        // 周二： val nextWednesdayOffset = if (dayOfWeek < 3) 3-dayOfWeek else 10 - dayOfWeek
        // 周三： val nextWednesdayOffset = if (dayOfWeek < 4) 4-dayOfWeek else 11 - dayOfWeek
        // 周四： val nextWednesdayOffset = if (dayOfWeek < 5) 5-dayOfWeek else 12 - dayOfWeek
        // 周五： val nextWednesdayOffset = if (dayOfWeek < 6) 6-dayOfWeek else 13 - dayOfWeek
        // 周六： val nextWednesdayOffset = if (dayOfWeek < 7) 7-dayOfWeek else 14 - dayOfWeek
        // 周日： val nextWednesdayOffset = if (dayOfWeek == 1) 0 else 8 - dayOfWeek

        // 增加到入参日期的下一个周几那天
        cd.add(Calendar.DAY_OF_MONTH, nextMondayOffset);
        return cd.getTime();
    }
    //计算下一次更新时间
    public static String nextUpdate(String one,String two,String there){
        if ("0".equals(one)){
            Integer amount = 0;
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.DAY_OF_MONTH) < Integer.valueOf(two)){
                amount = 0;
            }else if (calendar.get(Calendar.DAY_OF_MONTH) >= Integer.valueOf(two)){
                amount = 1;
            }
            for (int i = 0;i < 12;i++){
                try {
                    String preMonth = getPreMonth(amount);
                    if (Integer.valueOf(two) > 0 && Integer.valueOf(two) <= getMonthDays(preMonth)){
                        return preMonth + "-" + two + " " + there+":00";
                    }else {
                        amount++;
                    }
                }catch (Exception e){
                    break;
                }
            }
        }else if("1".equals(one)){
            return DateFormatUtils.format(dateToWeek(new Date(),Integer.valueOf(two)),yMd()) + " " + there+":00";
        }else if("2".equals(one)){
            return DateFormatUtils.format(tomorrow(new Date()),yMd()) + " " + there+":00";
        }
        return null;
    }
    /**
     * 返回明天
     * @param today
     * @return
     */
    public static Date tomorrow(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        return calendar.getTime();
    }
    /**
     * 判断是否为数字可以使用工具类 StringUtils
     * 通过方法 isNumeric 进行判断是否为数字
     * @param str
     * @return
     */
    private static Boolean isNumber(String str) {
        return StringUtils.isNumeric(str);
    }
    public static String aFewPlace(Integer integer){
        if (integer == 0){
            return "一等奖";
        }else if (integer == 1){
            return "二等奖";
        }else if (integer == 2){
            return "三等奖";
        }else if (integer == 3){
            return "四等奖";
        }else if (integer == 4){
            return "五等奖";
        }else if (integer == 5){
            return "六等奖";
        }else if (integer == 6){
            return "七等奖";
        }else if (integer == 7){
            return "安慰奖";
        }
        return "未中奖";
    }
    public static String getUUID(){
        String yyyyMMddHHmmss = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
        return yyyyMMddHHmmss + PanXiaoZhang.ran(6)+"y";
    }
    public static String getID(){
        UUID uuid = UUID.randomUUID();
        return String.valueOf(uuid);
    }
    public static List<String> getDays(String startTime, String endTime) {
        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date start = dateFormat.parse(startTime);
                Date end = dateFormat.parse(endTime);

                Calendar tempStart = Calendar.getInstance();
                tempStart.setTime(start);

                Calendar tempEnd = Calendar.getInstance();
                tempEnd.setTime(end);
                tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
                while (tempStart.before(tempEnd)) {
                    days.add(dateFormat.format(tempStart.getTime()));
                    tempStart.add(Calendar.DAY_OF_YEAR, 1);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            return days;
        }catch (Exception e){
            log.info("异常捕获{}",e.getMessage());
            return days;
        }
    }

    /**
     * 获取昨天
     * @return String
     * */
    public static String getYestoday(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    /**
     * 获取本月开始日期
     * @return String
     * **/
    public static String getMonthStart(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time)+" 00:00:00";
    }

    /**
     * 获取本月最后一天
     * @return String
     * **/
    public static String getMonthEnd(){
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time)+" 23:59:59";
    }

    /**
     * 获取本周的第一天
     * @return String
     * **/
    public static String getWeekStart(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time)+" 00:00:00";
    }

    /**
     * 获取本周的最后一天
     * @return String
     * **/
    public static String getWeekEnd(){
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time)+" 23:59:59";
    }

    /**
     * 获取本年的第一天
     * @return String
     * **/
    public static String getYearStart(){
        return new SimpleDateFormat("yyyy").format(new Date())+"-01-01 00:00:00";
    }

    /**
     * 获取本年的最后一天
     * @return String
     * **/
    public static String getYearEnd(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date currYearLast = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(currYearLast)+" 23:59:59";
    }
    public static Map timeToType(Map map){
        String timeToType = String.valueOf(map.get("timeToType"));
        if (isValueNull(map.get("timeToType"))){
            if (timeToType.equals("0")){
                map.put("startTime",DateFormatUtils.format(new Date(),PanXiaoZhang.yMd()) + " 00:00:00");
                map.put("endTime",DateFormatUtils.format(new Date(),PanXiaoZhang.yMd()) + " 23:59:59");
            }else if (timeToType.equals("1")){
                map.put("startTime",getWeekStart());
                map.put("endTime",getWeekEnd());
            }else if (timeToType.equals("2")){
                map.put("startTime",getMonthStart());
                map.put("endTime",getMonthEnd());
            }
        }
        return map;
    }
    //小写转大写
    public static char toUpperCase(char c1){
        int a = c1;//将接收到的小写字符c1转换成int类型的数据给a
        int b =a-32;     //a-32得出小写对应大写的ascll数值
        char bb = (char)b;//最后将b转换成char类型字符给bb
        return bb;        //返回bb；就是返回大写的字符

    }
    //大写转小写
    public static char toLowerCase(char c1){
        int a = c1;//将接收到的小写字符c1转换成int类型的数据给a
        int b =a+32;     //a+32得出大写写对应小写的ascll数值
        char bb = (char)b;//最后将b转换成char类型字符给bb
        return bb;        //返回bb；就是返回字母小写的字符

    }
    //截取大写字母
    public static String formatStr(String str){
        StringBuffer  sbf=new StringBuffer();
        for(int i=0;i<str.length();i++){
            char tempChr=str.charAt(i);
            if(tempChr>='A'&&tempChr<='Z'){
                sbf.append("_" + toLowerCase(tempChr));//如果是大写字母，则在字符前面插入一个空格
            }else {
                sbf.append(tempChr);
            }
        }
        return sbf.toString();
    }
    //遍历entity
    public static <T> T forEntity(Class<T> t){
        Class cls = t;
        Field[] declaredFields = cls.getDeclaredFields();
        for (int i = 0;i < declaredFields.length;i++){
            Field declaredField = declaredFields[i];
            declaredField.setAccessible(true);
            System.out.println("<result column=\""+formatStr(declaredField.getName())+"\" property=\""+declaredField.getName()+"\" />");
            //System.out.println("marketing_part."+formatStr(declaredField.getName())+" as m_"+formatStr(declaredField.getName()));
        }
        return null;
    }
    public static Boolean tableName(Object obj){
        String s = String.valueOf(obj);
        if (s.equals("general")){
            return true;
        }
        return s.equals("record");
    }
    public static String base64Str(String str){
        try {
            if (PanXiaoZhang.isNull(str)){
                return null;
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append("zhangxun");
            buffer.append("pansir");
            buffer.append("yingxiaoda");
            buffer.append(str);
            return Base64.getEncoder().encodeToString(buffer.toString().getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * 计算百分比
     * @param sum
     * @param number
     * @return
     */
    public static String percentage(Integer sum,Integer number){
        if (sum <= 0 || number <= 0){
            return 0+"%";
        }
        Double pct = (Double.valueOf(number) / Double.valueOf(sum)) * 100;
        Double aDouble = doubleD(pct, 2);
        String valueOf = String.valueOf(aDouble);
        //Integer pct = (number / sum) * 100;
        return valueOf+"%";
    }
    public static String comparisonOperators(String tou,String str){
        if (str.equals("e")){
            return tou+"TypeE";
        }else if (str.equals("r")){
            return tou+"TypeR";
        }else if (str.equals("l")){
            return tou+"TypeL";
        }else {
            return tou+"TypeR";
        }
    }
    public static String fuImg(String str){
        String[] split = str.split(",");
        if (split.length > 1){
            return split[0];
        }
        return str;
    }
    //时间格式
    public static String emdHmsy(){
        return "EEE MMM dd HH:mm:ss +0800 yyyy";
    }

    /**
     * 日期转化为Date类型
     * @param format
     * @param value
     * @return
     * @throws ParseException
     */
    public static Date string_to_timestamp(String format,String value) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.ENGLISH);
        return sdf.parse(value);
    }
    //地区数组
    public static String[] regions(){
        String[] strings = new String[]{"新疆","西藏","内蒙古","青海","四川","黑龙江","甘肃","云南","广西","湖南","陕西","广东","吉林","河北","湖北","贵州","山东","江西","河南","辽宁","山西","安徽","福建","浙江","江苏","重庆","宁夏","海南","台湾","北京","天津","上海","香港","澳门"};
        return strings;
    }
    /**
     * date2比date1多的天数
     * @param time1
     * @param time2
     * @return
     */
    public static Integer differentDays(Object time1,Object time2,String type) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        Date date1 = simpleDateFormat.parse(String.valueOf(time1));
        Date date2 = simpleDateFormat.parse(String.valueOf(time2));
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2) {//同一年
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            Integer i = timeDistance + (day2 - day1);
            if (i < 0){
                i = i * -1;
            }
            return i;
        } else {// 不同年
            //System.out.println("判断day2 - day1 : " + (day2-day1));
            int i = day2 - day1;
            if (i < 0){
                i = i * -1;
            }
            return i;
        }
    }

    /**
     * 生成兑换码
     * @param integer
     * @return
     */
    public static List<String> code_list(Integer integer){
        List<String> stringList = new ArrayList<>();
        for (int i = 0;i < integer;i++){
            stringList.add(getPassword(System.currentTimeMillis() + ran(4)));
        }
        return stringList;
    }
    /**
     * 获取属性名数组
     * */
    private static String[] getFiledName(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        for(int i=0;i<fields.length;i++){
// System.out.println(fields[i].getType());
            fieldNames[i]=fields[i].getName();
        }
        return fieldNames;
    }

    /* 根据属性名获取属性值
     * */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {

            return null;
        }
    }
    public static Map<String,String> entity_map(Object object){
        Map map = new HashMap();
        StringBuilder sbName = new StringBuilder();
        StringBuilder sbValue = new StringBuilder();
        String[] fieldNames = getFiledName(object);
        for(int j=0 ; j<fieldNames.length ; j++){ //遍历所有属性
            String name = fieldNames[j]; //获取属性的名字
            Object value = getFieldValueByName(name,object);
            //sbName.append(name);
            //sbValue.append(value);
            //if(j != fieldNames.length - 1) {
            //    sbName.append("/");
            //    sbValue.append("/");
            //}
            map.put(name,value);
        }
        //System.out.println("attribute name:"+sbName.toString());
        //System.out.println("attribute value:"+sbValue.toString());
        return map;
    }

    //判断那些字段不可为空
    public static ReturnEntity isNull(Object o1, Object o2){
        Map<String,String> not_null = PanXiaoZhang.entity_map(o2);
        for(Map.Entry<String,String> entry:not_null.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            String string = String.valueOf(PanXiaoZhang.getFieldValueByName(key, o1));
            if (String.valueOf(value).equals("isNotNullAndIsLengthNot0")){
                log.info(key+":"+string);
                if (string.equals("null") || string.length() < 1){
                    return new ReturnEntity(
                            CodeEntity.CODE_ERROR,
                            "",
                            "",
                            key + "不能为空",
                            0,
                            true
                    );
                }
            }
        };
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                "",
                "",
                MsgEntity.CODE_SUCCEED,
                0,
                false
        );
    }

    public static String JsonZiFuChuLi(String string){
        String[] split = string.split("\\\\");
        String str = "";
        for (String s : split){
            str += s;
        }

        String[] split_one = str.split("\"\\{");
        String str_one = "";
        for (Integer i = 0;i < split_one.length;i++){
            if (i + 1 < split_one.length){
                str_one += split_one[i]+"{";
            }else {
                str_one += split_one[i];
            }
        }
        String[] split_two = str_one.split("}\"");
        String str_two = "";
        for (Integer i = 0;i < split_two.length;i++){
            if (i + 1 < split_two.length){
                str_two += split_two[i]+"}";
            }else {
                str_two += split_two[i];
            }
        }
        return str_two;
    }
    //增删改的分类
    public static String authority_name(Integer integer){
        if (integer == 1){
            return "查询";
        }else if (integer == 2){
            return "添加";
        }else if (integer == 2){
            return "修改";
        }else if (integer == 2){
            return "删除";
        }
        return "";
    }
    //传入指定的日期beginDay和时间间隔days，往前为负数，往后为正数
    public static Date calculationDate(Date beginDay,long days){
        //获取指定日期的时间戳
        long beginTime= beginDay.getTime();
        //计算时间间隔的时间戳
        long intervalTime = days*24*60*60*1000;
        //用指定日期时间戳加上时间间隔得到所求的日期
        long lastTime = beginTime + intervalTime;
        //将所求日期的时间戳转为日期并返回
        return new Date(lastTime);
    }
    //获取token
    public static String getToken(){
        return HttpUtil.send("https://www.topvoyage.top/api/miniapp/v1/zhen_ning/get_access_token", "", "");
    }
    //获取token
    public static String getOpenId(String phone){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile",phone);
        String send = HttpUtil.send("https://www.topvoyage.top/api/miniapp/v1/zhen_ning/get_openid", jsonObject.toString(), "");
        return send;
    }
    //获取token
    public static String postOpenId(String token){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token",token);
        String send = HttpUtil.send("https://www.topvoyage.top/api/miniapp/v1/zhen_ning/get_openid_by_token", jsonObject.toString(), "");
        return send;
    }
    //发送消息
    public static ReturnEntity postWechat(String phone,String keyword1,String keyword2,String keyword3,String keyword4,String pagepath){
        try {
            Token token = JSONObject.parseObject(PanXiaoZhang.getToken(), Token.class);
            Token object = JSONObject.parseObject(PanXiaoZhang.getOpenId(phone), Token.class);
            if (object.getSuccess() != false && token.getSuccess() != false) {
                ReturnEntity entity = WechatMsg.tuiSongXiaoXi(
                        object.getResponse().getOpenid(),
                        keyword1,
                        keyword2,
                        keyword3,
                        keyword4,
                        "5_XBlqDRj5EQpliJcjCBoYrrKNiZAdOU54ZTX8H1Dvg",
                        token.getResponse().getAccess_token(),
                        pagepath
                );
                return entity;
            }else {
                Token admin = JSONObject.parseObject(PanXiaoZhang.getOpenId("15830024173"), Token.class);
                log.info("消息发送失败:{},{}",object,token);
                ReturnEntity entity = WechatMsg.tuiSongXiaoXi(
                        admin.getResponse().getOpenid(),
                        keyword1 + ",消息发送失败",
                        keyword2,
                        keyword3,
                        keyword4,
                        "5_XBlqDRj5EQpliJcjCBoYrrKNiZAdOU54ZTX8H1Dvg",
                        token.getResponse().getAccess_token(),
                        pagepath
                );
                return entity;
            }
        }catch (Exception e){
            log.info("捕获异常：{}",e.getMessage());
        }
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                "",
                "",
                MsgEntity.CODE_SUCCEED,
                0,
                false
        );
    }

    //发送消息
    public static ReturnEntity postWechatFer(String openId,String keyword1,String keyword2,String keyword3,String keyword4,String pagepath){
        try {
            ReturnEntity entity = new ReturnEntity();
            Token token = JSONObject.parseObject(PanXiaoZhang.getToken(), Token.class);
            if (!ObjectUtils.isEmpty(openId)){
                openId = openId.replaceAll("1234567.*","");
                if (!ObjectUtils.isEmpty(openId) && token.getSuccess() != false) {
                    entity = WechatMsg.tuiSongXiaoXi(
                            openId,
                            keyword1,
                            keyword2,
                            keyword3,
                            keyword4,
                            "5_XBlqDRj5EQpliJcjCBoYrrKNiZAdOU54ZTX8H1Dvg",
                            token.getResponse().getAccess_token(),
                            pagepath
                    );
                }
            }
            log.info("entity==================>{}",entity);
            if (ObjectUtils.isEmpty(entity.getCode()) || !entity.getCode().equals("0")){
                Token admin = JSONObject.parseObject(PanXiaoZhang.getOpenId("15830024173"), Token.class);
                log.info("消息发送失败:{},{}",openId,token);
                entity = WechatMsg.tuiSongXiaoXi(
                        admin.getResponse().getOpenid(),
                        keyword1,
                        keyword2,
                        keyword3,
                        keyword4,
                        "5_XBlqDRj5EQpliJcjCBoYrrKNiZAdOU54ZTX8H1Dvg",
                        token.getResponse().getAccess_token(),
                        pagepath
                );
                return entity;
            }
        }catch (Exception e){
            log.info("捕获异常：{}",e.getMessage());
        }
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                "",
                "",
                MsgEntity.CODE_SUCCEED,
                0,
                false
        );
    }
    /**
     * 判断是否是过去的日期
     * @param pastDate 输入的日期
     * @return 该日期晚于今日false
     * @return 该日期早于今日true
     */
    public static boolean isPastDate(Date pastDate){

        boolean flag = false;
        Date nowDate = new Date();
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
        //在日期字符串非空时执行
        //调用Date里面的before方法来做判断
        flag = pastDate.before(nowDate);
        return flag;
    }
    //判断用户状态
    public static ReturnEntity estimateState(SysPersonnel sysPersonnel){
        if (ObjectUtils.isEmpty(sysPersonnel)){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    "",
                    "",
                    "人员信息不存在",
                    0,
                    true
            );
        }
        if (sysPersonnel.getEmploymentStatus().equals(0) && !isPastDate(sysPersonnel.getLeaveTime())){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    "",
                    "",
                    "登录异常",
                    0,
                    true
            );
        }else if (sysPersonnel.getEmploymentStatus().equals(2)){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    "",
                    "",
                    "账号审核中",
                    0,
                    true
            );
        }
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                "",
                "",
                MsgEntity.CODE_SUCCEED,
                0,
                false
        );
    }

    public static Long getDayTime(String startTime,String endTime){

        // 解析日期字符串为LocalDate对象
        LocalDate localDate1 = LocalDate.parse(startTime, DateTimeFormatter.ISO_DATE);
        LocalDate localDate2 = LocalDate.parse(endTime, DateTimeFormatter.ISO_DATE);

        // 计算日期差异
        long daysBetween = ChronoUnit.DAYS.between(localDate1, localDate2);
        return daysBetween;
    }

    public static String GetNextDay(String dateString,Integer integer) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date;

        try {
            date = sdf.parse(dateString);
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, integer);
            date = calendar.getTime();
            String nextDay = sdf.format(date);
            return nextDay;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DateFormatUtils.format(new Date(),PanXiaoZhang.yMd());
    }

    /**
     * 两个日期相差多少年多少天
     * @param targetDate 目标日期 不能为null
     * @param compareDate 比较日期 不能为null
     * @return 相差年与天，返回绝对值或null，格式是28.096代表28年96天。
     */
    public static Integer ageYTime(LocalDate targetDate, LocalDate compareDate){
        try {
            if (compareDate == null) {
                return null;
            }
            if (targetDate == null) {
                return null;
            }
            int nyear = targetDate.getYear();
            int nm = targetDate.getMonthValue();
            int nd = targetDate.getDayOfMonth();

            int byear = compareDate.getYear();
            int bm = compareDate.getMonthValue();
            int bd = compareDate.getDayOfMonth();

            int year = nyear - byear;
            /*
             * 这段是处理两个日期相差多少年
             * 1、当前年份减去出生年份，比较当前月份是否小于出生月份，如果小于的话相差年数需要减1
             * 2、如果当前月份大于等于目标月份比较当月天，当前天数小于出生天相差年数减1
             */
            if (nm < bm) {
                year = year - 1;
            } else if (nm == bm) {
                if (nd < bd) {
                    year = year - 1;
                }
            }
            compareDate = compareDate.plusYears(year);
            float day = targetDate.toEpochDay() - compareDate.toEpochDay();
            day = day / 1000;//天数后移三位
            //return Math.abs(year + day);//取绝对值
            return year;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取出生日期
     *
     * @return 返回字符串类型
     */
    public String getBirthFromIdCard(String idCard) {
        if (idCard.length() != 18 && idCard.length() != 15) {
            return "请输入正确的身份证号码";
        }
        if (idCard.length() == 18) {
            String year = idCard.substring(6).substring(0, 4);// 得到年份
            String month = idCard.substring(10).substring(0, 2);// 得到月份
            String day = idCard.substring(12).substring(0, 2);// 得到日
            return (year + "-" + month + "-" + day);
        } else if (idCard.length() == 15) {
            String year = "19" + idCard.substring(6, 8);// 年份
            String month = idCard.substring(8, 10);// 月份
            String day = idCard.substring(10, 12);// 得到日
            return (year + "-" + month + "-" + day);
        }
        return null;
    }

    /**
     * 判断坐标是否在经纬度范围内
     * @param user 用户现在坐标
     * @param polyX 经度值X
     * @param polyY 纬度值Y
     * @return true表示在范围内，false表示不在范围内
     */
    public static boolean isLocationInRange(double[] user, double[] polyX, double[] polyY) {
        //return longitude >= MIN_LONGITUDE && longitude <= MAX_LONGITUDE && latitude >= MIN_LATITUDE
        //        && latitude <= MAX_LATITUDE;
        // 定义多边形顶点坐标
        //double[] polyX = {121.453601, 121.454908, 121.454153, 121.455159};
        //double[] polyY = {31.146111, 31.146482, 31.144543, 31.144856};
        // 定义用户当前位置坐标121.454948,31.145026
        //double[] user = {121.454845,31.144964};
        double userX = user[0];
        double userY = user[1];

        // 判断用户是否在多边形内部
        boolean isInside = false;
        int i, j = polyX.length - 1;
        for (i = 0; i < polyX.length; i++) {
            if ((polyY[i] < userY && polyY[j] >= userY || polyY[j] < userY && polyY[i] >= userY) && (polyX[i] <= userX || polyX[j] <= userX)) {
                if (polyX[i] + (userY - polyY[i]) / (polyY[j] - polyY[i]) * (polyX[j] - polyX[i]) < userX) {
                    isInside = !isInside;
                }
            }
            j = i;
        }

        if (isInside) {
            // 用户在多边形内部
            // 进行相应的操作
            return true;
        } else {
            // 用户不在多边形内部
            // 进行相应的操作
            return false;
        }
    }

    /**
     * 比较两个时间的先后顺序
     *
     * @param time1 第一个时间
     * @param time2 第二个时间
     * @return 如果 time1 在 time2 之前，返回-1；如果 time1 在 time2 之后，返回1；如果 time1 和 time2 相等，返回0。
     */
    public static int compareTime(LocalTime time1, LocalTime time2) {
        return time1.compareTo(time2);
    }

    /**
     * 判断是否历史日期
     * @param date
     * @return
     */
    public static Boolean compareDate(Date date){
            //LocalDate date = LocalDate.of(2020, 10, 1); // 给定日期
            LocalDate zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now(); // 当前日期
            System.out.println("日期" + zonedDateTime.compareTo(now));
            if (zonedDateTime.compareTo(now) < 0) {
                return true;
            } else {
                return false;
            }
    }
    public static double stringDouble(String str){
        BigDecimal decimal = new BigDecimal(str);
        double num = decimal.doubleValue();
        return num;
    }

    public static LocalTime dateLocalTime(String string) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = dateFormat.parse(string);
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        LocalTime localTime = zonedDateTime.toLocalTime();
        return localTime;
    }
    /**
     * 删除文件夹下的所有内容
     * @param file
     * @return
     */
    public static Boolean deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            System.out.println("文件删除失败,请检查文件是否存在以及文件路径是否正确");
            return false;
        }
        //获取目录下子文件
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                //递归删除目录下的文件
                deleteFile(f);
            } else {
                //文件删除
                f.delete();
                //打印文件名
                //System.out.println("文件名：" + f.getName());
            }
        }
        //文件夹删除
        file.delete();
        //System.out.println("目录名：" + file.getName());
        return true;
    }
    /**
     * 计算工作时长
     * @param workStartTime 工作开始时间
     * @param workEndTime 工作结束时间
     * @param startDateTime 上班时间
     * @param endDateTime 下班时间
     */
    public static Personnel getTime(LocalTime workStartTime, LocalTime workEndTime, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        //LocalDateTime startDateTime = LocalDateTime.of(2022, 3, 1, 9, 0); // 上班时间
        //LocalDateTime endDateTime = LocalDateTime.of(2022, 3, 3, 18, 0); // 下班时间
        //LocalTime workStartTime = LocalTime.of(9, 0); // 工作开始时间
        //LocalTime workEndTime = LocalTime.of(18, 0); // 工作结束时间
        long workDays = ChronoUnit.DAYS.between(startDateTime.toLocalDate(), endDateTime.toLocalDate()); // 工作天数
        Duration totalWorkDuration = Duration.ZERO; // 总工作时长
        for (int i = 0; i <= workDays; i++) {
            LocalDateTime date = startDateTime.plusDays(i);
            LocalDateTime workStartDateTime = LocalDateTime.of(date.toLocalDate(), workStartTime);
            LocalDateTime workEndDateTime = LocalDateTime.of(date.toLocalDate(), workEndTime);
            if (i == 0) { // 第一天
                workStartDateTime = startDateTime;
            } else if (i == workDays) { // 最后一天
                workEndDateTime = endDateTime;
            }
            Duration workDuration = Duration.between(workStartDateTime, workEndDateTime); // 计算工作时长
            totalWorkDuration = totalWorkDuration.plus(workDuration); // 累计总工作时长
        }
        System.out.println("工作天数：" + (workDays + 1) + "天");
        System.out.println("总工作时长：" + totalWorkDuration.toHours() + "小时" + totalWorkDuration.toMinutes() % 60 + "分钟");
        return new Personnel(String.valueOf(workDays + 1),totalWorkDuration.toHours() + "小时" + totalWorkDuration.toMinutes() % 60 + "分钟");
    }

    /**
     * 计算两个时间相差
     * @param startTime 预计抵达时间
     * @param endTime 抵达时间
     * @return
     */
    public static String getDayTime(LocalTime startTime,LocalTime endTime){
        //LocalTime startTime = LocalTime.of(9, 0); // 上班时间为 9:00
        //LocalTime endTime = LocalTime.of(17, 30); // 下班时间为 17:30

        Duration workingHours = Duration.between(startTime, endTime); // 计算工时
        //System.out.println(workingHours.toMinutes());
        return workingHours.toHours() + " 小时 " + workingHours.toMinutes() % 60 + " 分钟" + workingHours.getSeconds() % 60 + " 秒";
    }

    /**
     * 计算两个时间相差
     * @param startTime 预计抵达时间
     * @param endTime 抵达时间
     * @return
     */
    public static Long getMinuteTime(LocalTime startTime,LocalTime endTime){
        //LocalTime startTime = LocalTime.of(9, 0); // 上班时间为 9:00
        //LocalTime endTime = LocalTime.of(17, 30); // 下班时间为 17:30

        Duration workingHours = Duration.between(startTime, endTime); // 计算工时
        return workingHours.toMinutes();
    }

    /**
     * 判断某一个经纬度点是否在一组经纬度范围内
     *
     * @param ALon A点经度
     * @param ALat A点纬度
     * @param ps   范围多边形经纬度集合
     * @author Klay
     * @date 2023/2/8 18:06
     */
    public static boolean isPtInPoly(double ALon, double ALat, List<JqPoint> ps) {
        int iSum, iCount, iIndex;
        double dLon1 = 0, dLon2 = 0, dLat1 = 0, dLat2 = 0, dLon;
        if (ps.size() < 3) {
            return false;
        }
        iSum = 0;
        iCount = ps.size();
        for (iIndex = 0; iIndex < iCount; iIndex++) {
            if (iIndex == iCount - 1) {
                dLon1 = ps.get(iIndex).getX();
                dLat1 = ps.get(iIndex).getY();
                dLon2 = ps.get(0).getX();
                dLat2 = ps.get(0).getY();
            } else {
                dLon1 = ps.get(iIndex).getX();
                dLat1 = ps.get(iIndex).getY();
                dLon2 = ps.get(iIndex + 1).getX();
                dLat2 = ps.get(iIndex + 1).getY();
            }
            // 以下语句判断A点是否在边的两端点的水平平行线之间，在则可能有交点，开始判断交点是否在左射线上
            if (((ALat >= dLat1) && (ALat < dLat2)) || ((ALat >= dLat2) && (ALat < dLat1))) {
                if (Math.abs(dLat1 - dLat2) > 0) {
                    //得到 A点向左射线与边的交点的x坐标：
                    dLon = dLon1 - ((dLon1 - dLon2) * (dLat1 - ALat)) / (dLat1 - dLat2);
                    // 如果交点在A点左侧（说明是做射线与 边的交点），则射线与边的全部交点数加一：
                    if (dLon < ALon) {
                        iSum++;
                    }
                }
            }
        }
        if ((iSum % 2) != 0) {
            return true;
        }
        return false;
    }
    public static Integer eqTime(Date startDate,Date endDate){
        if (startDate.getTime() == endDate.getTime()) {
            return 0;
        } else if (startDate.getTime() > endDate.getTime()) {
            return 1;
        } else {
            return -1;
        }
    }
    public static Integer eqDate(Date date){
        LocalDate currentDate = LocalDate.now();
        LocalDate targetDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int compare = targetDate.compareTo(currentDate);
        if (compare == 0) {
            return 0;
        } else if (compare < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * 读取excel文件数据
     * @param path
     * @throws IOException
     * @throws BiffException
     */
    public static List<GetExcel> getExcel(String path) throws IOException, BiffException {
        //读取文件
        File file = new File(path);
        //用字符接文件数据
        FileInputStream fileInputStream = new FileInputStream(file);
        //解析字符流
        Workbook workbook = Workbook.getWorkbook(fileInputStream);
        //读取文件判断是什么工作簿
        Sheet sheet = workbook.getSheet(0);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();
        //返回所有内容
        List<GetExcel> stringList = new ArrayList<>();
        for (int j = 0; j < columns; j++) {
            //存储每行数据
            List<String> list = new ArrayList<>();
            //记录标题名称
            String title = sheet.getRow(0)[j].getContents();
            for (int i = 1; i < rows; i++) {
                //读取每行数据
                Cell[] row = sheet.getRow(i);
                try {
                    //获取可以查到的数据
                    byte[] bytes = row[j].getContents().getBytes(StandardCharsets.UTF_8);
                    //将数据添加至集合中
                    list.add(new String(bytes));
                    //数据打印
                    //log.info("contents:{}",contents);
                }catch (Exception e){
                    //数据获取失败
                    log.info("获取异常");
                }
            }
            //将数据存储到主体内容中
            stringList.add(new GetExcel(
                    title,
                    list
            ));
        }
        return stringList;
    }
    //判断是否成年
    /**
     *  根据身份证号判断当前年龄
     * @param cardNo
     * @return
     */
    public static int getAge(String cardNo) {
        String birthday = cardNo.substring(6, 14);
        Date birthdate = null;
        try {
            birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar currentDay = new GregorianCalendar();
        currentDay.setTime(birthdate);
        int birYear = currentDay.get(Calendar.YEAR);

        // 获取年龄
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String thisYear = simpleDateFormat.format(new Date());
        int age = Integer.parseInt(thisYear) - birYear;

        return age;
    }
    public static void main(String[] args) throws ParseException, IOException, BiffException {
        System.out.println(getAge("130430199910140513"));
        //ISysPersonnelMapper bean = GetSpringBean.getBean(ISysPersonnelMapper.class);
        //SysPersonnel personnel = bean.selectById(1);
        //System.out.println(personnel);
//        // 获取当前时间
//        Calendar calendar = Calendar.getInstance();
//        Date today = calendar.getTime();
//
//        // 将时间设置为昨天
//        calendar.add(Calendar.DATE, -1);
//        Date yesterday = calendar.getTime();
//        Boolean aBoolean = compareDate(yesterday);
//        System.out.println(aBoolean);
//        System.out.println(percent(0,1,1));

        //List<JqPoint> ps = new ArrayList<>();
        //JqPoint jqPoint1 = new JqPoint(34.272644,117.308166);
        //JqPoint jqPoint2 = new JqPoint(34.271483,117.301839);
        //JqPoint jqPoint3 = new JqPoint(34.263085,117.311008);
        //JqPoint jqPoint4 = new JqPoint(34.261602,117.304064);
        //
        //ps.add(jqPoint1);
        //ps.add(jqPoint2);
        //ps.add(jqPoint3);
        //ps.add(jqPoint4);
        //JqPoint n1 = new JqPoint(34.268167,117.303398);
        //JqPoint n2 = new JqPoint(34.265656,117.304198);
        //JqPoint n3 = new JqPoint(34.26336,117.306483);
        //JqPoint y1 = new JqPoint(34.263632,117.308461);
        //JqPoint y2 = new JqPoint(34.267525,117.308579);
        //JqPoint y4 = new JqPoint(34.269218,117.30799);
        //System.out.println("n1:" + isPtInPoly(n1.getX(), n1.getY(), ps));
        //System.out.println("n2:" + isPtInPoly(n2.getX(), n2.getY(), ps));
        //System.out.println("n3:" + isPtInPoly(n3.getX(), n3.getY(), ps));
        //System.out.println("y1:" + isPtInPoly(y1.getX(), y1.getY(), ps));
        //System.out.println("y2:" + isPtInPoly(y2.getX(), y2.getY(), ps));
        //System.out.println("y4:" + isPtInPoly(y4.getX(), y4.getY(), ps));
        //JSONObject jsonObject = new JSONObject();
        //jsonObject.put("personnelId",59);
        //jsonObject.put("x",34.268167);
        //jsonObject.put("y",117.303398);
        //String send = HttpUtil.send("https://truelemonweb.topvoyage.top/api/white_list/punching_card_record/area", jsonObject.toString(), "");
        //System.out.println(send);

        //Map<String,Integer> map = new HashMap<String,Integer>();
        //ReturnEntity entity = PanXiaoZhang.postWechatFer(
        //        "o_QtX5qJzKGc3YmCG2eUb-v5ZEm8",
        //        "",
        //        "",
        //        "补卡",
        //        "",
        //        "/pages/guide/guide?from=zn&redirect_url=/packageZn/pages/repair_check_list/repair_check_list?fromDispatchVerify=true"
        //);
        //System.out.println(entity);


        //LocalTime startTime = LocalTime.of(9, 0,3); // 上班时间为 9:00
        //LocalTime endTime = LocalTime.of(17, 30,28); // 下班时间为 17:30
        //
        //String dayTime = getDayTime(startTime, endTime);
        //System.out.println(dayTime);
    }
}
