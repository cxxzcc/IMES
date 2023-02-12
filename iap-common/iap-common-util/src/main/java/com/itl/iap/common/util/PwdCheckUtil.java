package com.itl.iap.common.util;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 密码强度校验
 * </p>
 *
 * @author kk
 * @date 2021/12/30 13:52
 */
public class PwdCheckUtil {

    /**
     * 检测密码中字符长度
     *
     * @return 符合长度要求 返回
     */
    public static boolean checkPasswordLength(String password) {
        boolean flag = false;

        // 如未设置最大长度，仅判断最小长度即可
        if ("".equals(PwdCheckConfig.MAX_LENGTH)) {
            if (password.length() >= Integer.parseInt(PwdCheckConfig.MIN_LENGTH)) {
                flag = true;
            }
        } else {
            if (password.length() >= Integer.parseInt(PwdCheckConfig.MIN_LENGTH)
                    && password.length() <= Integer.parseInt(PwdCheckConfig.MAX_LENGTH)) {
                flag = true;
            }
        }

        return flag;
    }

    /**
     * 检查密码中是否包含数字
     *
     * @return 包含数字 返回true
     */
    public static boolean checkContainDigit(String password) {
        char[] chPass = password.toCharArray();
        boolean flag = false;
        int num_count = 0;

        for (char pass : chPass) {
            if (Character.isDigit(pass)) {
                num_count++;
            }
        }
        if (num_count >= 1) {
            flag = true;
        }
        return flag;
    }

    /**
     * 检查密码中是否包含字母(不区分大小写)
     *
     * @return 包含字母 返回true
     */
    public static boolean checkContainCase(String password) {
        char[] chPass = password.toCharArray();
        boolean flag = false;
        int char_count = 0;

        for (char pass : chPass) {
            if (Character.isLetter(pass)) {
                char_count++;
            }
        }

        if (char_count >= 1) {
            flag = true;
        }

        return flag;
    }

    /**
     * 检查密码中是否包含小写字母
     *
     * @return 包含小写字母 返回true
     */
    public static boolean checkContainLowerCase(String password) {
        boolean flag = false;
        char[] chPass = password.toCharArray();
        int char_count = 0;

        for (char pass : chPass) {
            if (Character.isLowerCase(pass)) {
                char_count++;
            }
        }

        if (char_count >= 1) {
            flag = true;
        }

        return flag;
    }

    /**
     * 检查密码中是否包含大写字母
     *
     * @return 包含大写字母 返回true
     */
    public static boolean checkContainUpperCase(String password) {
        boolean flag = false;
        char[] chPass = password.toCharArray();
        int char_count = 0;

        for (char pass : chPass) {
            if (Character.isUpperCase(pass)) {
                char_count++;
            }
        }

        if (char_count >= 1) {
            flag = true;
        }

        return flag;
    }

    /**
     * 检查密码中是否包含特殊字符
     *
     * @return 包含特殊字符 返回true
     */
    public static boolean checkContainSpecialChar(String password) {
        boolean flag = false;
        char[] chPass = password.toCharArray();
        int special_count = 0;

        for (char pass : chPass) {
            if (PwdCheckConfig.CHECK_CONTAIN_SPECIAL_CHAR.indexOf(pass) != -1) {
                special_count++;
            }
        }

        if (special_count >= 1) {
            flag = true;
        }

        return flag;
    }

    /**
     * 键盘规则匹配器 横向连续检测
     *
     * @return 含有横向连续字符串 返回true
     */
    public static boolean checkLateralKeyboardSite(String password) {

        String t_password = password;
        // 将字符串内所有字符转为小写
        t_password = t_password.toLowerCase();
        int n = t_password.length();

        /*
         * 键盘横向规则检测
         */
        int arrLen = PwdCheckConfig.KEYBOARD_HORIZONTAL_ARR.length;
        int limit_num = Integer.parseInt(PwdCheckConfig.LIMIT_HORIZONTAL_NUM_KEY);

        for (int i = 0; i + limit_num <= n; i++) {
            String str = t_password.substring(i, i + limit_num);
            String distinguishStr = password.substring(i, i + limit_num);

            for (int j = 0; j < arrLen; j++) {
                String configStr = PwdCheckConfig.KEYBOARD_HORIZONTAL_ARR[j];
                String revOrderStr = new StringBuffer(PwdCheckConfig.KEYBOARD_HORIZONTAL_ARR[j]).reverse()
                        .toString();

                // 检查包含字母(区分大小写)
                if ("enable".equals(PwdCheckConfig.CHECK_DISTINGGUISH_CASE)) {
                    // 考虑 大写键盘匹配的情况
                    String upperStr = PwdCheckConfig.KEYBOARD_HORIZONTAL_ARR[j].toUpperCase();
                    if ((configStr.contains(distinguishStr)) || (upperStr.contains(distinguishStr))) {

                        return true;
                    }
                    // 考虑逆序输入情况下 连续输入
                    String revUpperStr = new StringBuffer(upperStr).reverse().toString();
                    if ((revOrderStr.contains(distinguishStr)) || (revUpperStr.contains(distinguishStr))) {
                        return true;
                    }
                } else {
                    if (configStr.contains(str)) {
                        return true;
                    }
                    // 考虑逆序输入情况下 连续输入
                    if (revOrderStr.contains(str)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 键盘规则匹配器 斜向规则检测
     *
     * @return 含有斜向连续字符串 返回true
     */
    public static boolean checkKeyboardSlantSite(String password) {
        String t_password = password;
        t_password = t_password.toLowerCase();
        int n = t_password.length();

        /*
         * 键盘斜线方向规则检测
         */
        int arrLen = PwdCheckConfig.KEYBOARD_SLOPE_ARR.length;
        int limit_num = Integer.parseInt(PwdCheckConfig.LIMIT_SLOPE_NUM_KEY);

        for (int i = 0; i + limit_num <= n; i++) {
            String str = t_password.substring(i, i + limit_num);
            String distinguishStr = password.substring(i, i + limit_num);
            for (int j = 0; j < arrLen; j++) {
                String configStr = PwdCheckConfig.KEYBOARD_SLOPE_ARR[j];
                String revOrderStr = new StringBuffer(PwdCheckConfig.KEYBOARD_SLOPE_ARR[j]).reverse().toString();
                // 检测包含字母(区分大小写)
                if ("enable".equals(PwdCheckConfig.CHECK_DISTINGGUISH_CASE)) {

                    // 考虑 大写键盘匹配的情况
                    String UpperStr = PwdCheckConfig.KEYBOARD_SLOPE_ARR[j].toUpperCase();
                    if ((configStr.contains(distinguishStr)) || (UpperStr.contains(distinguishStr))) {
                        return true;
                    }
                    // 考虑逆序输入情况下 连续输入
                    String revUpperStr = new StringBuffer(UpperStr).reverse().toString();
                    if ((revOrderStr.contains(distinguishStr)) || (revUpperStr.contains(distinguishStr))) {
                        return true;
                    }
                } else {
                    if (configStr.contains(str)) {
                        return true;
                    }
                    // 考虑逆序输入情况下 连续输入
                    if (revOrderStr.contains(str)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 评估a-z,z-a这样的连续字符
     *
     * @return 含有a-z,z-a连续字符串 返回true
     */
    public static boolean checkSequentialChars(String password) {
        String t_password = password;
        int limit_num = Integer.parseInt(PwdCheckConfig.LIMIT_LOGIC_NUM_CHAR);
        int normal_count;
        int reversed_count;

        // 检测包含字母(是否区分大小写)
        if ("disable".equals(PwdCheckConfig.CHECK_DISTINGGUISH_CASE)) {
            t_password = t_password.toLowerCase();
        }

        int n = t_password.length();
        char[] pwdCharArr = t_password.toCharArray();

        for (int i = 0; i + limit_num <= n; i++) {
            normal_count = 0;
            reversed_count = 0;
            for (int j = 0; j < limit_num - 1; j++) {
                if (pwdCharArr[i + j + 1] - pwdCharArr[i + j] == 1) {
                    normal_count++;
                    if (normal_count == limit_num - 1) {
                        return true;
                    }
                }

                if (pwdCharArr[i + j] - pwdCharArr[i + j + 1] == 1) {
                    reversed_count++;
                    if (reversed_count == limit_num - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 评估aaaa, 1111这样的相同连续字符
     *
     * @return 含有aaaa, 1111等连续字符串 返回true
     */
    public static boolean checkSequentialSameChars(String password) {
        int n = password.length();
        char[] pwdCharArr = password.toCharArray();
        int limit_num = Integer.parseInt(PwdCheckConfig.LIMIT_NUM_SAME_CHAR);
        int count;
        for (int i = 0; i + limit_num <= n; i++) {
            count = 0;
            for (int j = 0; j < limit_num - 1; j++) {
                if (pwdCharArr[i + j] == pwdCharArr[i + j + 1]) {
                    count++;
                    if (count == limit_num - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检测常用词库
     *
     * @return 含有常见词库 返回true
     */
    public static boolean checkSimpleWord(String password) {
        List<String> simpleWords = Arrays.asList(PwdCheckConfig.SIMPLE_WORDS);
        return simpleWords.contains(password.toLowerCase());
    }

    /**
     * 评估密码中包含的字符类型是否符合要求
     *
     * @return 符合要求 返回true
     */
    public static Boolean evalPassword(String password) {
        if (password == null || "".equals(password)) {
            throw new RuntimeException("密码不能为空");
        }
        boolean flag;

        /*
         * 检测长度
         */
        if ("enable".equals(PwdCheckConfig.CHECK_PASSWORD_LENGTH)) {
            flag = checkPasswordLength(password);
            if (!flag) {
                throw new RuntimeException("密码长度最小为6");
            }
        }

        /*
         * 检测包含数字
         */
        if ("enable".equals(PwdCheckConfig.CHECK_CONTAIN_DIGIT)) {
            flag = checkContainDigit(password);
            if (!flag) {
                throw new RuntimeException("密码应该包含数字,字母");
            }
        }

        /*
         * 检测包含字母
         */
        if ("enable".equals(PwdCheckConfig.CHECK_CONTAIN_CASE)) {
            flag = checkContainCase(password);
            if (!flag) {
                throw new RuntimeException("密码应该包含数字,字母");
            }
        }

        /*
         * 检测字母区分大小写
         */
        if ("enable".equals(PwdCheckConfig.CHECK_DISTINGGUISH_CASE)) {
            // 检测包含小写字母
            if ("enable".equals(PwdCheckConfig.CHECK_LOWER_CASE)) {
                flag = checkContainLowerCase(password);
                if (!flag) {
                    throw new RuntimeException("密码应该包含数字,字母");
                }
            }

            // 检测包含大写字母
            if ("enable".equals(PwdCheckConfig.CHECK_UPPER_CASE)) {
                flag = checkContainUpperCase(password);
                if (!flag) {
                    throw new RuntimeException("密码应该包含数字,字母");
                }
            }
        }

        /*
         * 检测包含特殊符号
         */
        if ("enable".equals(PwdCheckConfig.CHECK_CONTAIN_SPECIAL_CHAR)) {
            flag = checkContainSpecialChar(password);
            if (!flag) {
                throw new RuntimeException("密码应该包含特殊符号如 + - * /");
            }
        }

        /*
         * 检测键盘横向连续
         */
        if ("enable".equals(PwdCheckConfig.CHECK_HORIZONTAL_KEY_SEQUENTIAL)) {
            flag = checkLateralKeyboardSite(password);
            if (flag) {
                throw new RuntimeException("键盘布局顺序不应该用于密码");
            }
        }

        /*
         * 检测键盘斜向连续
         */
        if ("enable".equals(PwdCheckConfig.CHECK_SLOPE_KEY_SEQUENTIAL)) {
            flag = checkKeyboardSlantSite(password);
            if (flag) {
                throw new RuntimeException("键盘布局顺序不应该用于密码");
            }
        }

        /*
         * 检测逻辑位置连续
         */
        if ("enable".equals(PwdCheckConfig.CHECK_LOGIC_SEQUENTIAL)) {
            flag = checkSequentialChars(password);
            if (flag) {
                throw new RuntimeException("密码英文字母过于连续");
            }
        }

        /*
         * 检测相邻字符是否相同
         */
        if ("enable".equals(PwdCheckConfig.CHECK_SEQUENTIAL_CHAR_SAME)) {
            flag = checkSequentialSameChars(password);
            if (flag) {
                throw new RuntimeException("密码相邻字符重复过多");
            }
        }

        /*
         * 检测常用词库
         */
        if ("enable".equals(PwdCheckConfig.CHECK_SIMPLE_WORD)) {
            flag = checkSimpleWord(password);
            if (flag) {
                throw new RuntimeException("密码过于常用");
            }
        }

        return true;
    }

}
