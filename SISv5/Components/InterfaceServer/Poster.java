public class Poster
{
  private String year;
  private String category;
  private int count;

  public Poster(String year, String category, int count)
  {
    this.year = year;
    this.category = category;
    this.count = count;
  }

  public String getYear()
  {
    return year;
  }

  public void setYear(String year)
  {
    this.year = year;
  }

  public String getCategory()
  {
    return category;
  }

  public void setCategory(String category)
  {
    this.category = category;
  }

  public int getCount()
  {
    return count;
  }

  public void setCount(int count)
  {
    this.count = count;
  }
}
