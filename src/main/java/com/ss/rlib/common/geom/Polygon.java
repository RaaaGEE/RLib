package com.ss.rlib.common.geom;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Geometry 3D polygon.
 * 
 * @author zcxv
 * @date 27.09.2018
 */
public class Polygon {
	private final static float EPSILON_ON_PLANE = 0.1f;
	private final static float EPSILON_CWW = 0.01f;
	
	/** 
	 * The polygon vertices. 
	 */
	private final Vector3f[] vertices;
	
	/** 
	 * The polygon plane.
	 */
	private final Plane plane;
	
	/** 
	 * Custom flags.
	 */
	private int flags;
	
	/**
	 * Construct polygon from vertices.
	 * 
	 * @param vertices vertices
	 * @exception RuntimeException throw if vertices is less than 3
	 * @see Polygon#Polygon(Vector3f, Vector3f, Vector3f)
	 * @see Polygon#Polygon(float, float, float, float, float, float, float, float, float)
	 */
	public Polygon(@NotNull Vector3f[] vertices) throws RuntimeException {
		if(vertices.length < 3) {
			throw new RuntimeException("Polygon cannot have less than 3 vertices.");
		}
		
		this.vertices = vertices;
		
		final Vector3f normal = new Vector3f(0, 0, 0);
		for(int i = 2; i < vertices.length; i++) {
			final Vector3f ab = new Vector3f(vertices[i - 1]).subtractLocal(vertices[0]);
			final Vector3f ac = new Vector3f(vertices[i]).subtractLocal(vertices[0]);
			normal.addLocal(ab.cross(ac));
		}
		normal.normalizeLocal();
		plane = new Plane(getPlanePoint(), normal);
	}
	
	/**
	 * Construct polygon from 3 vertices.
	 * 
	 * @param a first vertex
	 * @param b second vertex
	 * @param c third vertex
	 * @see Polygon#Polygon(Vector3f[])
	 * @see Polygon#Polygon(float, float, float, float, float, float, float, float, float)
	 */
	public Polygon(@NotNull Vector3f a, @NotNull Vector3f b, @NotNull Vector3f c) {
		this(new Vector3f[] { a, b, c });
	}
	
	/**
     * Construct polygon from 3 vertices.
     * 
     * @param x1 first vertex x axis component
     * @param y1 first vertex y axis component
     * @param z1 first vertex z axis component
     * @param x1 second vertex x axis component
     * @param y1 second vertex y axis component
     * @param z1 second vertex z axis component
     * @param x1 third vertex x axis component
     * @param y1 third vertex y axis component
     * @param z1 third vertex z axis component
     * @see Polygon#Polygon(Vector3f, Vector3f, Vector3f)
     * @see Polygon#Polygon(Vector3f[])
     */
	public Polygon(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
		this(new Vector3f(x1, y1, z1), new Vector3f(x2, y2, z2), new Vector3f(x3, y3, z3));
	}
	
	/**
	 * Return plane point.<br>
	 * It's a first polygon vertex.
	 * 
	 * @return plane point
	 */
	public @NotNull Vector3f getPlanePoint() {
		return vertices[0];
	}
	
	/**
	 * Return polygon vertices.
	 * 
	 * @return vertices
	 */
	public @NotNull Vector3f[] getVertices() {
	    return vertices;
	}
	
	/**
	 * Return polygon plane.
	 * 
	 * @return plane
	 */
	public @NotNull Plane getPlane() {
	    return plane;
	}
	
	/**
	 * Return polygon custom flags.<br>
	 * By default it is zero.
	 * 
	 * @return flags
	 */
	public int getFlags() {
	    return flags;
	}
	
	/**
	 * Set polygon custom flags.
	 * 
	 * @param flags flags
	 */
	public void setFlags(int flags) {
	    this.flags = flags;
	}
	
	/**
	 * Toggle flag bit.
	 * 
	 * @param flag flag
	 * @return flag status
	 */
	public boolean toggleFlag(int flag) {
	    flags ^= flag;
	    return isFlagSet(flag);
	}
	
	/**
	 * Return flag status.
	 * 
	 * @param flag
	 * @return true if flag is set
	 */
	public boolean isFlagSet(int flag) {
	    return (flags & flag) != 0;
	}
	
	/**
	 * Set flag bit.
	 * 
	 * @param flag
	 */
	public void setFlag(int flag) {
	    flags |= flag;
	}
	
	/**
	 * Unset flag bit.
	 * 
	 * @param flag flag
	 */
	public void unsetFlag(int flag) {
	    flags &= ~flag;
	}
	
	/**
	 * Return mid-point of this polygon.
	 * 
	 * @return mid-point
	 */
	public @NotNull Vector3f getMidPoint() {
		final Vector3f point = new Vector3f();
		for(int i = 0; i < vertices.length; i++) {
			point.addLocal(vertices[i]);
		}
		return point.divideLocal(vertices.length);
	}
	
	/**
	 * Check polygon vertices to coplanar.
	 * 
	 * @return true if polygon vertices is coplanar
	 */
	public boolean isCoplanar() {
		for(int i = 0; i < vertices.length; i++) {
			if(!isOnPlane(vertices[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Determines if point on polygon plane.
	 * 
	 * @param point point
	 * @return true if point on plane
	 */
	public boolean isOnPlane(@NotNull Vector3f point) {
		final float distance = plane.distance(point);
		return distance > -EPSILON_ON_PLANE && distance < EPSILON_ON_PLANE;
	}
	
	/**
	 * Determines if line AB intersect polygon.<br> 
	 * If point isn't null and line intersect polygon then point coordinates is set to intersection.
	 * 
	 * @param a start line point
	 * @param b end line point
	 * @param point [out] point with intersection coordinates, can be null
	 * @return true if line AB intersect polygon
	 */
	public boolean intersect(@NotNull Vector3f a, @NotNull Vector3f b, @Nullable Vector3f point) {
		final float aDistance = plane.distance(a, vertices[0]);
		final float bDistance = plane.distance(b, vertices[0]);
		if((aDistance < 0 && bDistance < 0) || (aDistance > 0 && bDistance > 0)) {
			return false;
		}
		
		final Vector3f intersection = plane.rayIntersection(a, b, vertices[0]);
		if(intersection.equals(a, 0.1f) || intersection.equals(b, 0.1f)) {
			return false;
		}
		
		if(point != null) {
			point.set(intersection);
		}
		
		return contains(intersection);
	}
	
	/**
	 * Determines if point inside of polygon.
	 * 
	 * @param point point
	 * @return true if point inside
	 */
	public boolean contains(@NotNull Vector3f point) {
		int low = 0;
		int high = vertices.length;
		
		do {
			int mid = (low + high) / 2;
			if(isTriangleCCW(vertices[0], vertices[mid], point)) {
				low = mid;
			} else {
				high = mid;
			}
		} while(low + 1 < high);
		
		if(low == 0 || high == vertices.length) {
			return false;
		}
		
		return isTriangleCCW(vertices[low], vertices[high], point);
	}
	
	/** 
	 * Determines if triangle specified by 3 points is defined counterclockwise.
	 * 
	 * @param a first vertex
	 * @param b second vertex
	 * @param c third vertex
	 * @return true if defined
	 */
	private boolean isTriangleCCW(@NotNull Vector3f a, @NotNull Vector3f b, @NotNull Vector3f c) {
		final Vector3f ab = new Vector3f(a).subtractLocal(b);
		final Vector3f ac = new Vector3f(a).subtractLocal(c);
		ab.crossLocal(ac);
		
		return plane.getNormal().dot(ab) > EPSILON_CWW;
	}
	
}
