class MyCube {
  
  float sz, xm, ym, zm;
  MyCube(float sz, float xm, float ym, float zm) {
    this.sz = sz;
    this.xm = xm;
    this.ym = ym;
    this.zm = zm;
  }

  void render() {
    translate(xm, ym, zm);
    float chance = random(1.0);
    if (chance > 0.4) {
      box(sz);
      smooth();
      boxCount++;
    }
    else {
      sphere(sz/2);
      sphereCount++;
    }
  }
}

