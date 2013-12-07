package sagma.core.data;

import sagma.core.math.Vec3;

public class PointOctree {
	private PointOctree XYZ;
	private PointOctree XYD;
	private PointOctree XHZ;
	private PointOctree XHD;
	private PointOctree WYZ;
	private PointOctree WYD;
	private PointOctree WHZ;
	private PointOctree WHD;
	
	private Vec3 data;
	private int index;
	
	public int size() {
		if (data == null) return 0;
		int i = 1;
		if (XYZ != null) i += XYZ.size();
		if (XYD != null) i += XYD.size();
		if (XHZ != null) i += XHZ.size();
		if (XHD != null) i += XHD.size();
		if (WYZ != null) i += WYZ.size();
		if (WYD != null) i += WYD.size();
		if (WHZ != null) i += WHZ.size();
		if (WHD != null) i += WHD.size();
		return i;
	}
	
	public void add(Vec3 v, int ind) {
		if (data == null) {
			data = v;
			this.index = ind;
		} else {
			if (v.x < data.x) {
				if (v.y < data.y) {
					if (v.z < data.z) {
						if (XYZ == null) XYZ = new PointOctree();
						XYZ.add(v, ind);
					} else {
						if (XYD == null) XYD = new PointOctree();
						XYD.add(v, ind);
					}
				} else {
					if (v.z < data.z) {
						if (XHZ == null) XHZ = new PointOctree();
						XHZ.add(v, ind);
					} else {
						if (XHD == null) XHD = new PointOctree();
						XHD.add(v, ind);
					}
				}
			} else {
				if (v.y < data.y) {
					if (v.z < data.z) {
						if (WYZ == null) WYZ = new PointOctree();
						WYZ.add(v, ind);
					} else {
						if (WYD == null) WYD = new PointOctree();
						WYD.add(v, ind);
					}
				} else {
					if (v.z < data.z) {
						if (WHZ == null) WHZ = new PointOctree();
						WHZ.add(v, ind);
					} else {
						if (WHD == null) WHD = new PointOctree();
						WHD.add(v, ind);
					}
				}
			}
		}
	}
	
	public int find(Vec3 v) {
		if (data == null) {
			return -1;
		}
		
		if (data.equals(v)) return index;
		if (v.x < data.x) {
			if (v.y < data.y) {
				if (v.z < data.z) {
					if (XYZ == null) return -1;
					return XYZ.find(v);
				}
				if (XYD == null) return -1;
				return XYD.find(v);
			}
			if (v.z < data.z) {
				if (XHZ == null) return -1;
				return XHZ.find(v);
			}
			if (XHD == null) return -1;
			return XHD.find(v);
		}
		if (v.y < data.y) {
			if (v.z < data.z) {
				if (WYZ == null) return -1;
				return WYZ.find(v);
			}
			if (WYD == null) return -1;
			return WYD.find(v);
		}
		if (v.z < data.z) {
			if (WHZ == null) return -1;
			return WHZ.find(v);
		}
		if (WHD == null) return -1;
		return WHD.find(v);
	}
	
	
}
