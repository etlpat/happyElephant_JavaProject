/**
 * 日期格式化工具类
 */
export default class DateUtils {
  /**
   * 格式化为年月日时分秒（yyyy-MM-dd HH:mm:ss）
   * @param {string|Date} date - Java Date字符串或Date对象
   * @returns {string} 格式化后的日期字符串
   */
  static formatDateTime(date) {
    if (!date) return "";

    const d = new Date(date);
    if (isNaN(d.getTime())) return "";

    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, "0");
    const day = String(d.getDate()).padStart(2, "0");
    const hours = String(d.getHours()).padStart(2, "0");
    const minutes = String(d.getMinutes()).padStart(2, "0");
    const seconds = String(d.getSeconds()).padStart(2, "0");

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  }

  /**
   * 格式化为年月日（yyyy-MM-dd）
   * @param {string|Date} date - Java Date字符串或Date对象
   * @returns {string} 格式化后的日期字符串
   */
  static formatDate(date) {
    if (!date) return "";

    const d = new Date(date);
    if (isNaN(d.getTime())) return "";

    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, "0");
    const day = String(d.getDate()).padStart(2, "0");

    return `${year}-${month}-${day}`;
  }

  /**
   * 格式化为自定义格式
   * @param {string|Date} date - Java Date字符串或Date对象
   * @param {string} format - 格式字符串，如 'yyyy/MM/dd HH:mm'
   * @returns {string} 格式化后的日期字符串
   */
  static format(date, formatStr) {
    if (!date) return "";

    const d = new Date(date);
    if (isNaN(d.getTime())) return "";

    const map = {
      yyyy: d.getFullYear(),
      MM: String(d.getMonth() + 1).padStart(2, "0"),
      dd: String(d.getDate()).padStart(2, "0"),
      HH: String(d.getHours()).padStart(2, "0"),
      mm: String(d.getMinutes()).padStart(2, "0"),
      ss: String(d.getSeconds()).padStart(2, "0"),
    };

    return formatStr.replace(/yyyy|MM|dd|HH|mm|ss/g, (match) => map[match]);
  }
}
